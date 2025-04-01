//배너와 배너 레이아웃 중간자 역할하는 클래스
package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentBannerBinding

class BannerFragment(val imgRes : Int): Fragment() { //리소스를 매개변수로 받음
    lateinit var binding: FragmentBannerBinding //viewbinding 으로 xml과 연결

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBannerBinding.inflate(inflater, container, false) //fragment 레이아웃 binding 객체 생성
        binding.bannerImageIv.setImageResource(imgRes) //전달받은 이미지 view를 ImageView에 설정
        return binding.root
    }
}
