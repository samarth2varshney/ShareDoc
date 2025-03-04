package com.example.sharedoc

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.ByteArrayOutputStream
import java.net.URI

class MainActivity : AppCompatActivity() {

    private lateinit var webSocketClient: MyWebSocketClient
    private val IMAGE_PICK_CODE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.connect).setOnClickListener {
            val name = findViewById<EditText>(R.id.name).text.toString()
            val serverUri = URI("ws://10.22.4.181:8080/ws?userId=$name")
            webSocketClient = MyWebSocketClient(serverUri) { bitmap ->
                runOnUiThread {
                    if (bitmap != null) {
                        findViewById<ImageView>(R.id.imageView).setImageBitmap(bitmap) // Display the received image
                    } else {
                        Log.e("WebSocket", "Failed to decode image")
                    }
                }
            }
            webSocketClient.connect()
        }


        findViewById<Button>(R.id.btnSelectImage).setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_PICK_CODE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                webSocketClient.sendJson(mapOf(
                    "action" to "set_recipient",
                    "to" to "sagar",
                    "data" to "hello bro"
                ))
                sendImage(uri)
            }
        }
    }

    fun sendImage(uri: Uri) {
        val inputStream = contentResolver.openInputStream(uri)
        val buffer = ByteArray(8192) // Send 8KB chunks
        var bytesRead: Int

        while (inputStream?.read(buffer).also { bytesRead = it ?: -1 } != -1) {
            webSocketClient.sendBinaryData(buffer.copyOf(bytesRead))
        }

        inputStream?.close()
        webSocketClient.sendBinaryData("END".toByteArray())
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocketClient.close()
    }

}