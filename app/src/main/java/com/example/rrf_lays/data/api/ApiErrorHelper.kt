package com.example.rrf_lays.data.api

import android.util.Log
import retrofit2.Response

object ApiErrorHelper {
    fun <T> parseError(response: Response<T>): String {
        val code = response.code()
        val errorBody = response.errorBody()?.string() ?: "Unknown error"
        Log.e("Supabase", "Error Code: $code, Body: $errorBody")

        return when (code) {
            409 -> "Data sudah ada / username sudah dipakai"
            401, 403 -> "API key salah atau schema belum di-expose"
            else -> "Error $code: $errorBody"
        }
    }
}
