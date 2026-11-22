package com.example.rrf_lays.data.api

import com.example.rrf_lays.data.model.CatFactModel
import retrofit2.http.GET

interface CatFactApiService {
    @GET("fact")
    suspend fun getCatFact(): CatFactModel
}
