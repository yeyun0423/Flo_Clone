package com.example.flo.ui.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.R
import com.example.network.AuthService
import com.example.network.LoginRequest
import com.example.network.LoginResponse
import com.example.network.LoginView
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import java.security.MessageDigest

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
        val kakaoLoginBtn = findViewById<ImageView>(R.id.kakaoLoginButton)

        // 해시키 출력
        try {
            val info = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
            } else {
                packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            }

            val signatures = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P)
                info.signingInfo.apkContentsSigners else info.signatures

            for (signature in signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hash = Base64.encodeToString(md.digest(), Base64.NO_WRAP)
                Log.d("KeyHash", "해시키: $hash")
            }
        } catch (e: Exception) {
            Log.e("KeyHash", "해시키 추출 실패", e)
        }

        // 일반 로그인
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

        // 회원가입 이동
        signUpBtn.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }

        // 카카오 로그인
        kakaoLoginBtn.setOnClickListener {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                Log.d(TAG, "카카오톡 로그인 시도")
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        Log.e(TAG, "카카오톡 로그인 실패 → 계정 로그인 fallback", error)
                        UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
                            handleKakaoLogin(token, error)
                        }
                    } else {
                        handleKakaoLogin(token, null)
                    }
                }
            } else {
                Log.d(TAG, "카카오톡 미설치 → 계정 로그인 시도")
                UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
                    handleKakaoLogin(token, error)
                }
            }
        }
    }

    // 일반 로그인 성공 시
    override fun onLoginSuccess(response: LoginResponse) {
        val result = response.result
        if (result == null) {
            Toast.makeText(this, "로그인 실패: 결과 누락", Toast.LENGTH_SHORT).show()
            return
        }

        val accessToken = result.accessToken
        getSharedPreferences("auth", MODE_PRIVATE).edit().putString("jwt", accessToken).apply()

        Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()
        moveToMain()
    }

    // 일반 로그인 실패 시
    override fun onLoginFailure(message: String) {
        Toast.makeText(this, "로그인 실패: $message", Toast.LENGTH_SHORT).show()
    }

    // 카카오 로그인 성공 시
    private fun handleKakaoLogin(token: OAuthToken?, error: Throwable?) {
        if (error != null) {
            Log.e(TAG, "카카오 로그인 실패", error)
            Toast.makeText(this, "카카오 로그인 실패", Toast.LENGTH_SHORT).show()
            return
        }

        if (token != null) {
            Log.d(TAG, "카카오 로그인 성공, accessToken: ${token.accessToken}")

            UserApiClient.instance.me { user, error ->
                if (user != null) {
                    val nickname = user.kakaoAccount?.profile?.nickname ?: "사용자"
                    Toast.makeText(this, "${nickname}님 환영합니다!", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "➡ MainActivity로 이동 전")

                    // JWT로 쓸 accessToken을 SharedPreferences에 저장
                    getSharedPreferences("auth", MODE_PRIVATE)
                        .edit()
                        .putString("jwt", token.accessToken)
                        .apply()

                    moveToMain()
                } else {
                    Log.e(TAG, "카카오 사용자 정보 가져오기 실패", error)
                }
            }
        }
    }

    // 메인 화면 이동
    private fun moveToMain() {
        Log.d(TAG, "🔥 moveToMain() 실행됨")
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
