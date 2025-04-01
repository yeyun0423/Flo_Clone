package com.example.flo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {
    lateinit var binding: ActivitySongBinding           //나중에 초기화를 해줄게!! (var : 다시 초기화O  val :다시 초기화 X)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater) //메모리에 객체화하는 것
        setContentView(binding.root) //이 xml 파일을 마음대로 쓸거다!!


        binding.songDownIb.setOnClickListener {
            finish()
        }

        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(false)
        }

        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(true)
        }


        binding.songRepeatIv.setOnClickListener {
            setReapetStatus(false)
        }

        binding.songRepeatBlackIv.setOnClickListener {
            setReapetStatus(true)
        }

        // 랜덤 아이콘 클릭 리스너
        binding.songRandomIv.setOnClickListener {
            setRandomStatus(false)
        }

        binding.songRandomBlackIv.setOnClickListener {
            setRandomStatus(true)
        }

        //**DataClass를 활용한 intent 데이터 전송 방법
        if (intent.hasExtra("title") && intent.hasExtra("singer")) {
            binding.songMusicTitleTv.text = intent.getStringExtra("title") // 텍스트 뷰를 바꿀건데 인텐트에서 타이틀이라는 키값을 가진 스트링으로 바꿔줄 것이다.
            binding.songSingerNameTv.text = intent.getStringExtra("singer")
        }
    }

    fun setPlayerStatus(isPlaying: Boolean) {
        if (isPlaying) {
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
        } else {
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
        }
    }

    fun setReapetStatus(isPlaying: Boolean) {
        if (isPlaying) {
            binding.songRepeatIv.visibility = View.VISIBLE
            binding.songRepeatBlackIv.visibility = View.GONE
        } else {
            binding.songRepeatIv.visibility = View.GONE
            binding.songRepeatBlackIv.visibility = View.VISIBLE
        }
    }

    fun setRandomStatus(isPlaying: Boolean) {
        if (isPlaying) {
            binding.songRandomIv.visibility = View.VISIBLE
            binding.songRandomBlackIv.visibility = View.GONE
        } else {
            binding.songRandomIv.visibility = View.GONE
            binding.songRandomBlackIv.visibility = View.VISIBLE
        }
    }
}
