package com.example.network

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)