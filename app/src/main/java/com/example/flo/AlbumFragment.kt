package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentAlbumBinding

class AlbumFragment : Fragment() {

    private lateinit var binding: FragmentAlbumBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        // HomeFragment에서 전달받은 값
        val title = arguments?.getString("title")
        val singer = arguments?.getString("singer")
        val coverImg = arguments?.getInt("coverImg")

        // UI에 값 세팅
        binding.albumMusicTitleTv.text = title ?: "제목 없음"
        binding.albumSingerNameTv.text = singer ?: "가수 없음"
        coverImg?.let {
            binding.albumAlbumIv.setImageResource(it)
        }

        // 뒤로가기 버튼 누르면 HomeFragment로 전환
        binding.albumBackIv.setOnClickListener {
            (requireActivity() as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
        }

        return binding.root
    }
}
