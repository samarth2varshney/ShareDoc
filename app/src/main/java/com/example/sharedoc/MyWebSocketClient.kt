package com.example.sharedoc

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.google.gson.Gson
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.net.URI
import java.nio.ByteBuffer

class MyWebSocketClient(serverUri: URI, private val onImageReceived: (Bitmap?) -> Unit,private val sendImage:()->Unit) : WebSocketClient(serverUri) {

    data class socketMessgae(val action: String, val to: String, val data: String,val form:String,val fileSize:Int)

    private val outputStream = ByteArrayOutputStream() // Store image data

    override fun onOpen(handshakedata: ServerHandshake?) {
        Log.d("WebSocket", "Connected to server")
    }

    override fun onMessage(message: String?) {
        val m = Gson().fromJson(message, socketMessgae::class.java)
        Log.d("WebSocket", "Received message: ${m.action}")
        when (m.action){
           "send_image"->{sendImage()}
            else->{}
        }
    }

    override fun onMessage(bytes: ByteBuffer?) {
        bytes?.let {
            val receivedData = ByteArray(it.remaining())
            it.get(receivedData)

            // Check for "END" signal
            val endMarker = "END".toByteArray(Charsets.UTF_8)
            if (receivedData.contentEquals(endMarker)) {
                Log.d("WebSocket", "Image transfer complete. Decoding...")

                val byteArray = outputStream.toByteArray()
                val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

                onImageReceived(bitmap) // Display the received image
                outputStream.reset() // Clear for next transfer
            } else {
                outputStream.write(receivedData) // Append received data
            }
        }
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        Log.d("WebSocket", "Closed: $reason")
    }

    override fun onError(ex: Exception?) {
        Log.e("WebSocket", "Error: ${ex?.message}")
    }

    fun sendBinaryData(data: ByteArray) {
        send(data)
        Log.d("WebSocket", "Sent binary data: ${data.size} bytes")
    }

    fun sendJson(data: Map<String, Any>) {
        val json = JSONObject(data).toString()
        send(json)
        Log.d("WebSocket", "Sent JSON: $json")
    }

}