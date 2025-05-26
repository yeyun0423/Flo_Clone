package com.example.network

import com.example.flo.RegisterResult

data class RegisterResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: RegisterResult?
)