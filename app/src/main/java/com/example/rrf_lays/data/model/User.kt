package com.example.rrf_lays.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("nama") val nama: String,
    @SerializedName("no_hp") val no_hp: String,
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String? = null,
    @SerializedName("created_at") val created_at: String? = null
)
