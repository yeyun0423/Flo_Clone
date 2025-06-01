package com.example.flo.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BannerVpAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val fragmentList = mutableListOf<Fragment>() //배너로 사용할 플래그먼트들 저장하는 리스트

    override fun createFragment(position: Int): Fragment = fragmentList[position]

    override fun getItemCount(): Int = fragmentList.size //총 몇개의 플래그먼트 슬라이드를 반환할 것인지

    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment) //새로운 배너 추가
        notifyItemInserted(fragmentList.size - 1) //데이터 변경 시 화면에 즉시 반영
    }
}