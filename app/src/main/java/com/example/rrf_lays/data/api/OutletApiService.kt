package com.example.rrf_lays.data.api

import com.example.rrf_lays.data.model.Outlet
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface OutletApiService {
    @GET("outlet")
    suspend fun getAll(
        @Query("select") select: String = "*",
        @Query("order") order: String? = null
    ): Response<List<Outlet>>

    @POST("outlet")
    @Headers("Prefer: return=representation")
    suspend fun create(@Body outlet: Outlet): Response<List<Outlet>>

    @PATCH("outlet")
    suspend fun update(
        @Query("id") idFilter: String,
        @Body outlet: Outlet
    ): Response<Unit>

    @DELETE("outlet")
    suspend fun delete(
        @Query("id") idFilter: String
    ): Response<Unit>
}
