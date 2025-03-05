package com.example.sharedoc

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.sharedoc.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URI

class MainActivity : AppCompatActivity() {

    private lateinit var webSocketClient: MyWebSocketClient
    private val IMAGE_PICK_CODE = 1000
    private lateinit var imageUri: Uri
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.connect.setOnClickListener {
            val name = findViewById<EditText>(R.id.name).text.toString()
            val serverUri = URI("ws://10.22.2.38:8080/ws?userId=$name")
            webSocketClient = MyWebSocketClient(serverUri,{ bitmap ->
                runOnUiThread {
                    if (bitmap != null) {
                        binding.imageView.setImageBitmap(bitmap) // Display the received image
                    } else {
                        Log.e("WebSocket", "Failed to decode image")
                    }
                }
            },{sendImage(imageUri)},{
                progress->updateProgressUI(progress)
            })
            webSocketClient.connect()
        }


        binding.btnSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_PICK_CODE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                val fileSize = contentResolver.openFileDescriptor(uri, "r")?.statSize ?: -1
                webSocketClient.sendJson(mapOf(
                    "action" to "set_recipient",
                    "to" to "sagar",
                    "data" to "hello bro",
                    "fileSize" to fileSize,
                ))
                imageUri = uri
            }
        }
    }

    fun sendImage(uri: Uri) {
        Log.i("MainActivity","sendImage function called")
        val inputStream = contentResolver.openInputStream(uri)
        val buffer = ByteArray(8192) // Send 8KB chunks
        val fileSize = contentResolver.openFileDescriptor(uri, "r")?.statSize ?: -1
        var totalBytesSent = 0L
        var bytesRead: Int

        while (inputStream?.read(buffer).also { bytesRead = it ?: -1 } != -1) {
            webSocketClient.sendBinaryData(buffer.copyOf(bytesRead))
            totalBytesSent += bytesRead
            val progress = (totalBytesSent * 100) / fileSize
            updateProgressUI(progress)
        }

        inputStream?.close()
        webSocketClient.sendBinaryData("END".toByteArray())
    }

    fun updateProgressUI(progress: Long) {
        runOnUiThread {
            binding.progressBar.progress = progress.toInt()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocketClient.close()
    }

}