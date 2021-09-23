package com.example.f1racingcompanion.utils

import com.google.gson.Gson
import java.io.ByteArrayOutputStream
import android.util.*
import okhttp3.HttpUrl

import java.util.zip.Inflater

object LiveTimingUtils {
    fun createHubMessage(method: String, args: List<String>): String {
        return Gson().toJson(
            mutableMapOf(
                'H' to Constants.HUB_NAME,
                'M' to method,
                'A' to args,
                'I' to 1
            )
        )
    }

    fun decodeMessage(text: String): String {
        val byteArray = Base64.decode(text, Base64.DEFAULT) // I relay that given text is correct b64 encoded
        return byteArray.zlibDecompress()
    }

    fun createWebSocketUrl(token: String, connectionData: String = "[{\"name\": \"Streaming\"}]"): String {
        val url = HttpUrl.Builder()
            .scheme("https")
            .host(Constants.WEBSCOKET_HOST)
            .addPathSegment(Constants.WEBSOCKET_SIGNALR_PATH)
            .addPathSegment(Constants.WEBSOCKET_CONNECT_PATH)
            .addQueryParameter("transport", Constants.WEBSCOKET_TRANSPORT)
            .addQueryParameter("connectionToken", token)
            .addQueryParameter("connectionData", connectionData)
            .addQueryParameter("clientProtocol", Constants.WEBSCOKET_PROTOCOL)
            .build().toUrl()
        val res = url.toString().replace("https", "wss")
        return res
    }

}
fun ByteArray.zlibDecompress(): String {
    val inflater = Inflater(true) // Given data is without any header - only raw data

    val decompressedStream = ByteArrayOutputStream()

    return decompressedStream.use {
        val buffer = ByteArray(1024)

        inflater.setInput(this)

        // Decompress data in chunks of 1024 bytes until end of the data
        var count = -1
        while (count != 0) {
            count = inflater.inflate(buffer)
            decompressedStream.write(buffer, 0, count)
        }

        inflater.end()

        decompressedStream.toString(Charsets.UTF_8.name())
    }
}