package com.example.flo

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.network.*

class LoginActivity : AppCompatActivity(), LoginView {

    private lateinit var authService: AuthService

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

            val request = LoginRequest(fullEmail, password)
            authService.login(request, this)
        }

        signUpBtn.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
    }

    override fun onLoginSuccess(response: LoginResponse) {
        val accessToken = response.result?.accessToken ?: ""
        val prefs = getSharedPreferences("auth", MODE_PRIVATE)
        prefs.edit().putString("jwt", accessToken).apply()

        Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onLoginFailure(message: String) {
        Toast.makeText(this, "로그인 실패: $message", Toast.LENGTH_SHORT).show()
    }
}
