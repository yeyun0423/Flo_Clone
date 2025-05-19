package com.example.flo

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var emailDomainSpinner: Spinner
    private lateinit var passwordInput: EditText
    private lateinit var loginBtn: Button
    private lateinit var db: AppDatabase
    private lateinit var signUpBtn: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailInput = findViewById(R.id.emailInput)
        emailDomainSpinner = findViewById(R.id.emailDomainSpinner)
        passwordInput = findViewById(R.id.passwordInput)
        loginBtn = findViewById(R.id.loginBtn)
        signUpBtn = findViewById(R.id.signUpText)

        db = AppDatabase.getInstance(this)

        loginBtn.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val domain = emailDomainSpinner.selectedItem.toString()
            val password = passwordInput.text.toString().trim()
            val fullEmail = if (domain == "직접입력") email else "$email@$domain"

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = db.userDao().getUser(fullEmail, password)

            if (user != null) {
                //  JWT(=userIdx) 저장
                val prefs = getSharedPreferences("auth", MODE_PRIVATE)
                prefs.edit().putInt("jwt", user.id).apply()

                Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "이메일 또는 비밀번호가 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        signUpBtn.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
        }

    }
