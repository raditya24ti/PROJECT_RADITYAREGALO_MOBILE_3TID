package com.example.rrf_lays.data.api

import com.example.rrf_lays.data.model.Produk
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface ProdukApiService {
    @GET("produk")
    suspend fun getAll(
        @Query("select") select: String = "*",
        @Query("order") order: String? = null
    ): Response<List<Produk>>

    @POST("produk")
    @Headers("Prefer: return=representation")
    suspend fun create(@Body produk: Produk): Response<List<Produk>>

    @PATCH("produk")
    suspend fun update(
        @Query("id") idFilter: String,
        @Body produk: Produk
    ): Response<Unit>

    @DELETE("produk")
    suspend fun delete(
        @Query("id") idFilter: String
    ): Response<Unit>
}
