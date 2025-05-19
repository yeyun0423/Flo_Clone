package com.example.flo

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class SignUpActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

            db = AppDatabase.getInstance(this)
            userDao = db.userDao()

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
                Toast.makeText(this, "이메일과 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != passwordConfirm) {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (userDao.getUserByEmail(fullEmail) != null) {
                Toast.makeText(this, "이미 존재하는 이메일입니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newUser = User(email = fullEmail, password = password)
            userDao.insert(newUser)
            Toast.makeText(this, "회원가입 완료!", Toast.LENGTH_SHORT).show()

            // 로그인 화면으로 이동
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
