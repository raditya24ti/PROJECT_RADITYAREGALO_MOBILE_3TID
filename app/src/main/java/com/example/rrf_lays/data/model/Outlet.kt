package com.example.rrf_lays.data.model

import com.google.gson.annotations.SerializedName

data class Outlet(
    @SerializedName("id") val id: Long?,
    @SerializedName("nama") val nama: String,
    @SerializedName("alamat") val alamat: String,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("jam_buka") val jam_buka: String?,
    @SerializedName("no_telp") val no_telp: String?,
    @SerializedName("created_at") val created_at: String?
)
