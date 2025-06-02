package com.example.flo.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.flo.R
import com.example.flo.databinding.FragmentProfileBinding
import com.kakao.sdk.user.UserApiClient
import android.content.Intent
import com.example.flo.ui.activity.LoginActivity



class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 사용자 정보 가져오기
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("ProfileFragment", "사용자 정보 요청 실패", error)
                binding.profileNickname.text = "정보를 가져올 수 없습니다"
            } else if (user != null) {
                val nickname = user.kakaoAccount?.profile?.nickname ?: "닉네임 없음"
                val email = user.kakaoAccount?.email ?: "이메일 없음"
                val profileImgUrl = user.kakaoAccount?.profile?.thumbnailImageUrl

                binding.profileNickname.text = nickname
                binding.profileEmail.text = email

                if (profileImgUrl != null) {
                    Glide.with(this)
                        .load(profileImgUrl)
                        .circleCrop()
                        .into(binding.profileImage)
                } else {
                    binding.profileImage.setImageResource(R.drawable.ic_default_profile)
                }
            }
        }

        // 로그아웃 버튼 클릭 처리
        binding.btnLogout.setOnClickListener {
            // 로그아웃 성공 시 이동
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Log.e("ProfileFragment", "로그아웃 실패", error)
                    Toast.makeText(requireContext(), "로그아웃 실패", Toast.LENGTH_SHORT).show()
                } else {
                    Log.i("ProfileFragment", "로그아웃 성공")
                    Toast.makeText(requireContext(), "로그아웃 되었습니다", Toast.LENGTH_SHORT).show()

                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    requireActivity().finish()
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
