package com.example.rrf_lays.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.rrf_lays.R

object ImageLoader {
    fun ImageView.loadGambar(gambar: String?) {
        if (gambar.isNullOrBlank()) {
            setImageResource(R.drawable.logo)
            return
        }

        if (gambar.startsWith("http", ignoreCase = true)) {
            Glide.with(context)
                .load(gambar)
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(this)
        } else {
            val resId = context.resources.getIdentifier(gambar, "drawable", context.packageName)
            if (resId != 0) {
                setImageResource(resId)
            } else {
                setImageResource(R.drawable.logo)
            }
        }
    }
}
