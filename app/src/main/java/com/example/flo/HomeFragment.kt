package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.homeAlbumTodayPlay.setOnClickListener {

            val song = Song(
                title = "사랑이라 했던 말 속에서",
                singer = "캔트비블루",
                second = 0,
                playTime = 180,
                isPlaying = true
            )

            // 현재 재생 곡으로 설정
            SongDatabase.currentSong = song

            // MainActivity의 MiniPlayer UI 갱신
            (activity as? MainActivity)?.updateMiniplayerUI()
        }

        return binding.root
    }
}
