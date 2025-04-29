package com.example.flo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var isPlaying: Boolean = false
    private var currentSecond: Int = 0
    private val totalDuration: Int = 60

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
                    setPlayerStatus(false)
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

        binding.mainMiniplayerBtn.setOnClickListener {
            setPlayerStatus(true)
            handler.post(updateRunnable)
        }

        binding.mainPauseBtn.setOnClickListener {
            setPlayerStatus(false)
            handler.removeCallbacks(updateRunnable)
        }

        binding.mainPlayerCl.setOnClickListener {
            moveToSongActivity()
        }
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
        binding.mainMiniplayerSb.max = totalDuration
        updateSeekBar()
    }

    private fun setPlayerStatus(isPlaying: Boolean) {
        this.isPlaying = isPlaying
        if (isPlaying) {
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE
        } else {
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE
        }
    }

    private fun updateSeekBar() {
        binding.mainMiniplayerSb.progress = currentSecond
        binding.mainMiniplayerStartTv.text = formatTime(currentSecond)
        binding.mainMiniplayerEndTv.text = formatTime(totalDuration)
    }

    private fun moveToSongActivity() {
        val song = Song(
            binding.mainMiniplayerTitleTv.text.toString(),
            binding.mainMiniplayerSingerTv.text.toString(),
            currentSecond,
            totalDuration,
            isPlaying
        )

        val intent = Intent(this, SongActivity::class.java)
        intent.putExtra("title", song.title)
        intent.putExtra("singer", song.singer)
        intent.putExtra("second", song.second)
        intent.putExtra("playTime", song.playTime)
        intent.putExtra("isPlaying", song.isPlaying)
        startActivity(intent)
    }

    private fun formatTime(seconds: Int): String {
        val min = seconds / 60
        val sec = seconds % 60
        return String.format("%02d:%02d", min, sec)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateRunnable)
    }
}
