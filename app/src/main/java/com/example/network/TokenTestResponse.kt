package com.example.network

data class TokenTestResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: String?
)