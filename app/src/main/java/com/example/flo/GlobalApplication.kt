    package com.example.flo
    import android.app.Application
    import com.kakao.sdk.common.KakaoSdk

    class GlobalApplication : Application() {
        override fun onCreate() {
            super.onCreate()
            KakaoSdk.init(this,"redirect_url")
        }
    }