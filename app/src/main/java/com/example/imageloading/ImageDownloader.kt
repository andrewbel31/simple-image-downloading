package com.example.imageloading

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object ImageDownloader {

    private val executorService: ExecutorService = Executors.newCachedThreadPool()
    private val cache = ConcurrentHashMap<String, Bitmap?>()

    fun download(url: String, into: ImageView) {
        if (cache[url] != null) {
            into.setImageBitmap(cache[url])
            return
        }
        val imageDownloadRunnable =
            ImageDownloadRunnable(
                into,
                url,
                cache
            )

        into.addOnAttachStateChangeListener(
            object : View.OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(v: View?) {

                }

                override fun onViewDetachedFromWindow(v: View?) {
                    into.removeOnAttachStateChangeListener(this)
                    into.post { into.setImageResource(0) }
                }
            })
        executorService.submit(imageDownloadRunnable)
    }
}