package com.example.network

import com.example.network.LoginRequest
import com.example.network.LoginResponse
import com.example.network.RegisterRequest
import com.example.network.RegisterResponse
import com.example.network.TokenTestResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    @POST("/join")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("/test")
    fun testToken(@Header("Authorization") token: String): Call<TokenTestResponse>
}