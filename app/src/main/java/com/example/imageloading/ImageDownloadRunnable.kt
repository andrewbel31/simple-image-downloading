package com.example.imageloading

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.ImageView
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ConcurrentHashMap

class ImageDownloadRunnable(
    private val target: ImageView,
    private val url: String,
    private val cache: ConcurrentHashMap<String, Bitmap?>
) : Runnable {

    override fun run() {
        val bitmap =
            syncLoadImage(url, target.width, target.height).also {
                if (it != null) {
                    cache[url] = it
                }
            }

        if (bitmap != null) {
            target.post { target.setImageBitmap(bitmap) }
        }
    }

    private fun syncLoadImage(imageUrl: String, width: Int, height: Int): Bitmap? {
        val url = URL(imageUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connect()

        if (connection.responseCode == HttpURLConnection.HTTP_OK) {
            connection.inputStream.use {
                val original = BitmapFactory.decodeStream(it)
                Log.d("ImageDownloader", "download success for $imageUrl")
                return Bitmap.createScaledBitmap(original, width, height, false)
            }
        }

        return null
    }
}