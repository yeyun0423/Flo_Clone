package com.example.flo.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.R
import com.example.flo.ui.activity.SignUpActivity
import com.example.network.AuthService
import com.example.network.LoginRequest
import com.example.network.LoginResponse
import com.example.network.LoginView

class LoginActivity : AppCompatActivity(), LoginView {

    private lateinit var authService: AuthService
    private val TAG = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        authService = AuthService()

        val emailInput = findViewById<EditText>(R.id.emailInput)
        val emailDomainSpinner = findViewById<Spinner>(R.id.emailDomainSpinner)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        val signUpBtn = findViewById<TextView>(R.id.signUpText)

        loginBtn.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val domain = emailDomainSpinner.selectedItem.toString()
            val fullEmail = "$email@$domain"
            val password = passwordInput.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d(TAG, "로그인 요청: email=$fullEmail, password=$password")

            val request = LoginRequest(fullEmail, password)
            authService.login(request, this)
        }

        signUpBtn.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
    }

    override fun onLoginSuccess(response: LoginResponse) {
        Log.d(TAG, "로그인 응답 성공: $response")

        val result = response.result
        if (result == null) {
            Log.e(TAG, "로그인 결과가 null입니다.")
            Toast.makeText(this, "로그인 실패: 결과 누락", Toast.LENGTH_SHORT).show()
            return
        }

        val accessToken = result.accessToken
        Log.d(TAG, "받은 accessToken: $accessToken")

        val prefs = getSharedPreferences("auth", MODE_PRIVATE)
        prefs.edit().putString("jwt", accessToken).apply()

        Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()

        try {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } catch (e: Exception) {
            Log.e(TAG, "MainActivity로 이동 중 오류", e)
            Toast.makeText(this, "메인 화면 이동 중 오류 발생", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onLoginFailure(message: String) {
        Log.e(TAG, "로그인 실패: $message")
        Toast.makeText(this, "로그인 실패: $message", Toast.LENGTH_SHORT).show()
    }
}