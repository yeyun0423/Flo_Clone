package com.example.network

interface SignUpView {
    fun onSignUpSuccess(response: RegisterResponse)
    fun onSignUpFailure(message: String)
}