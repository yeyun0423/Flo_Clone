package com.example.network

interface LoginView {
    fun onLoginSuccess(response: LoginResponse)
    fun onLoginFailure(message: String)
}