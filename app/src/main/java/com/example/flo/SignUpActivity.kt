package com.example.flo

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.network.*

class SignUpActivity : AppCompatActivity(), SignUpView {

    private lateinit var authService: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        authService = AuthService()

        val emailInput = findViewById<EditText>(R.id.emailInput)
        val domainSpinner = findViewById<Spinner>(R.id.emailDomainSpinner)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val passwordConfirmInput = findViewById<EditText>(R.id.passwordConfirmInput)
        val signUpBtn = findViewById<Button>(R.id.btnSignUp)

        signUpBtn.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val domain = domainSpinner.selectedItem.toString()
            val fullEmail = "$email@$domain"
            val password = passwordInput.text.toString()
            val passwordConfirm = passwordConfirmInput.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "이메일과 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != passwordConfirm) {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 이름은 빈 문자열로 처리 (API 필수 아님)
            val request = RegisterRequest(
                name = "",
                email = fullEmail,
                password = password
            )

            authService.register(request, this)
        }
    }

    override fun onSignUpSuccess(response: RegisterResponse) {
        Toast.makeText(this, "회원가입 성공!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onSignUpFailure(message: String) {
        Toast.makeText(this, "회원가입 실패: $message", Toast.LENGTH_SHORT).show()
    }
}
