package com.example.network

import com.example.network.LoginRequest
import com.example.network.LoginResponse
import com.example.network.LoginView
import com.example.network.RegisterRequest
import com.example.network.RegisterResponse
import com.example.network.SignUpView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthService {

    private val api = NetworkManager.retrofit.create(AuthApi::class.java)

    fun register(request: RegisterRequest, view: SignUpView) {
        api.register(request).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    view.onSignUpSuccess(response.body()!!)
                } else {
                    view.onSignUpFailure("회원가입 실패: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                view.onSignUpFailure("서버 오류: ${t.message}")
            }
        })
    }

    fun login(request: LoginRequest, view: LoginView) {
        api.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    view.onLoginSuccess(response.body()!!)
                } else {
                    view.onLoginFailure("로그인 실패: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                view.onLoginFailure("서버 오류: ${t.message}")
            }
        })
    }
}