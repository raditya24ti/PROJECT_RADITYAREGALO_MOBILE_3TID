package com.example.rrf_lays.data.api

import com.example.rrf_lays.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApiService {
    @POST("users")
    @Headers("Prefer: return=representation")
    suspend fun register(@Body user: User): Response<List<User>>

    @GET("users")
    suspend fun findByUsername(
        @Query("username") usernameFilter: String,
        @Query("select") select: String = "id,username"
    ): Response<List<User>>

    @GET("users")
    suspend fun login(
        @Query("username") usernameFilter: String,
        @Query("password") passwordFilter: String,
        @Query("select") select: String = "id,nama,no_hp,username"
    ): Response<List<User>>

    @GET("users")
    suspend fun count(
        @Query("select") select: String = "id"
    ): Response<List<User>>
}
