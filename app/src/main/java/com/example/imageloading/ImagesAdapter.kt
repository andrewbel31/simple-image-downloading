package com.example.imageloading

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ImagesAdapter(
    private val data: List<Image>
) : RecyclerView.Adapter<ImagesAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(itemView = view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imageView = itemView.findViewById<ImageView>(R.id.imageView)
        private val text = itemView.findViewById<TextView>(R.id.textView)

        fun bind(image: Image) {
            ImageDownloader.download(
                url = image.url,
                into = imageView
            )
            text.text = image.text
        }
    }

    data class Image(
        val url: String,
        val text: String
    )

}