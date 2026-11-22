package com.example.rrf_lays.photo

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rrf_lays.R
import com.example.rrf_lays.data.model.PhotoModel
import com.example.rrf_lays.databinding.ItemPhotoBinding

class PhotoAdapter(
    private val items: List<PhotoModel>,
    private val isHorizontal: Boolean = false
) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    inner class PhotoViewHolder(val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        if (isHorizontal) {
            binding.root.layoutParams = RecyclerView.LayoutParams(
                260.dpToPx(parent.context),
                RecyclerView.LayoutParams.WRAP_CONTENT
            )
        }
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tvAuthor.text = item.author
        Glide.with(holder.itemView.context)
            .load(item.download_url)
            .override(800, 533)
            .centerCrop()
            .placeholder(R.drawable.logo)
            .into(holder.binding.imgPhoto)
    }

    override fun getItemCount(): Int = items.size

    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }
}
