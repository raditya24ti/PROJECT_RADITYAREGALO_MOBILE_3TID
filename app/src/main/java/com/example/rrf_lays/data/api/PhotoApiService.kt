package com.example.rrf_lays.data.api

import com.example.rrf_lays.data.model.PhotoModel
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotoApiService {
    @GET("list")
    suspend fun getPhotos(@Query("limit") limit: Int = 12): List<PhotoModel>
}
