package com.example.flo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isPlaying = false
    private var currentSecond = 0
    private val totalDuration = 180

    private val handler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            if (isPlaying) {
                if (currentSecond < totalDuration) {
                    currentSecond++
                    updateSeekBar()
                    handler.postDelayed(this, 1000)
                } else {
                    isPlaying = false
                    togglePlayPauseUI()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_FLO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomNavigation()
        initMiniplayer()
    }

    private fun initBottomNavigation() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    true
                }
                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    true
                }
                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    true
                }
                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    true
                }
                else -> false
            }
        }
    }

    private fun initMiniplayer() {
        binding.mainMiniplayerBtn.setOnClickListener {
            isPlaying = true
            togglePlayPauseUI()
            handler.post(updateRunnable)
        }

        binding.mainPauseBtn.setOnClickListener {
            isPlaying = false
            togglePlayPauseUI()
            handler.removeCallbacks(updateRunnable)
        }

        binding.mainPlayerCl.setOnClickListener {
            if (SongDatabase.currentSong == null) {
                SongDatabase.currentSong = Song(
                    binding.mainMiniplayerTitleTv.text.toString(),
                    binding.mainMiniplayerSingerTv.text.toString(),
                    currentSecond,
                    totalDuration,
                    isPlaying
                )
            } else {
                SongDatabase.currentSong!!.second = currentSecond
                SongDatabase.currentSong!!.isPlaying = isPlaying
            }

            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }

        binding.mainMiniplayerSb.max = totalDuration
        updateSeekBar()
    }

    private fun togglePlayPauseUI() {
        if (isPlaying) {
            binding.mainMiniplayerBtn.visibility = android.view.View.GONE
            binding.mainPauseBtn.visibility = android.view.View.VISIBLE
        } else {
            binding.mainMiniplayerBtn.visibility = android.view.View.VISIBLE
            binding.mainPauseBtn.visibility = android.view.View.GONE
        }
    }

    private fun updateSeekBar() {
        binding.mainMiniplayerSb.progress = currentSecond
        binding.mainMiniplayerStartTv.text = formatTime(currentSecond)
        binding.mainMiniplayerEndTv.text = formatTime(totalDuration)

        SongDatabase.currentSong?.second = currentSecond
    }

    private fun formatTime(seconds: Int): String {
        val min = seconds / 60
        val sec = seconds % 60
        return String.format("%02d:%02d", min, sec)
    }

    override fun onResume() {
        super.onResume()
        currentSecond = SongDatabase.currentSong?.second ?: 0
        isPlaying = SongDatabase.currentSong?.isPlaying ?: false
        updateSeekBar()
        togglePlayPauseUI()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateRunnable)
    }

    fun updateMiniplayerUI() {
        val song = SongDatabase.currentSong
        if (song != null) {
            binding.mainMiniplayerTitleTv.text = song.title
            binding.mainMiniplayerSingerTv.text = song.singer
            currentSecond = song.second
            isPlaying = song.isPlaying
            updateSeekBar()
            togglePlayPauseUI()

            // 바가 움직이도록 재생 타이머 시작
            if (isPlaying) {
                handler.removeCallbacks(updateRunnable)
                handler.post(updateRunnable)
            }
        }
    }


}
