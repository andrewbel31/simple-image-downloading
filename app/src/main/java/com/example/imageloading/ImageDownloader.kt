package com.example.imageloading

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object ImageDownloader {

    private val executorService: ExecutorService = Executors.newCachedThreadPool()
    private val cache = ConcurrentHashMap<String, Bitmap?>()
    private val handler = Handler(Looper.getMainLooper())

    fun download(url: String, into: ImageView) {
        if (cache[url] != null) {
            into.setImageBitmap(cache[url])
            return
        }
        val imageDownloadRunnable =
            ImageDownloadRunnable(
                into,
                url,
                cache,
                handler
            )

        into.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View?) {

            }

            override fun onViewDetachedFromWindow(v: View?) {
                into.removeOnAttachStateChangeListener(this)
                into.setImageResource(0)
                handler.removeCallbacks(imageDownloadRunnable)
            }
        })
        executorService.submit(imageDownloadRunnable)
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