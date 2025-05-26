package com.example.network

import android.util.Log
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthService {

    private val api = NetworkManager.retrofit.create(AuthApi::class.java)

    fun register(request: RegisterRequest, view: SignUpView) {
        api.register(request).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    Log.d("AuthService", "회원가입 성공: ${response.body()}")
                    view.onSignUpSuccess(response.body()!!)
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("AuthService", "회원가입 실패 응답: $errorBody")
                    val message = try {
                        JSONObject(errorBody ?: "").optString("message", "알 수 없는 오류")
                    } catch (e: Exception) {
                        "에러 응답 파싱 실패"
                    }
                    view.onSignUpFailure("회원가입 실패: $message")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e("AuthService", "회원가입 서버 오류: ${t.message}", t)
                view.onSignUpFailure("서버 오류: ${t.message}")
            }
        })
    }

    fun login(request: LoginRequest, view: LoginView) {
        api.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    Log.d("AuthService", "로그인 성공: ${response.body()}")
                    view.onLoginSuccess(response.body()!!)
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("AuthService", "로그인 실패 응답: $errorBody")
                    val message = try {
                        JSONObject(errorBody ?: "").optString("message", "알 수 없는 오류")
                    } catch (e: Exception) {
                        "에러 응답 파싱 실패"
                    }
                    view.onLoginFailure("로그인 실패: $message")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("AuthService", "로그인 서버 오류: ${t.message}", t)
                view.onLoginFailure("서버 오류: ${t.message}")
            }
        })
    }
}
