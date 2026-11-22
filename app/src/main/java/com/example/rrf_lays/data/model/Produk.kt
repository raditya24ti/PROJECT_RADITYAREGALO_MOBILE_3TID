package com.example.rrf_lays.data.model

import com.google.gson.annotations.SerializedName

data class Produk(
    @SerializedName("id") val id: Long?,
    @SerializedName("nama") val nama: String,
    @SerializedName("berat") val berat: String,
    @SerializedName("harga") val harga: Int,
    @SerializedName("deskripsi") val deskripsi: String?,
    @SerializedName("gambar") val gambar: String?,
    @SerializedName("link_shopee") val link_shopee: String?,
    @SerializedName("created_at") val created_at: String?
)
