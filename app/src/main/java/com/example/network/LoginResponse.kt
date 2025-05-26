package com.example.network

import com.example.flo.LoginResult

data class LoginResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: LoginResult?
)