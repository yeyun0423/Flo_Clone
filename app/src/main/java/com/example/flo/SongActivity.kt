package com.example.flo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySongBinding
    private lateinit var timer: Timer
    private var song: Song? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        song = SongDatabase.currentSong

        binding.songDownIb.setOnClickListener {
            finish()
        }

        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(false)
        }

        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(true)
        }

        binding.songRandomIv.setOnClickListener {
            restartTimer()
        }

        initPlayer()
        startTimer()
    }

    private fun initPlayer() {
        song?.let {
            binding.songMusicTitleTv.text = it.title
            binding.songSingerNameTv.text = it.singer
            binding.songEndTimeTv.text = formatTime(it.playTime)
            binding.songStartTimeTv.text = formatTime(it.second)
            binding.songProgressSb.max = it.playTime
            binding.songProgressSb.progress = it.second
            setPlayerStatus(it.isPlaying)
        }
    }

    private fun setPlayerStatus(isPlaying: Boolean) {
        song?.isPlaying = isPlaying
        if (::timer.isInitialized) {
            timer.isPlaying = isPlaying
        }

        if (isPlaying) {
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
        } else {
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
        }
    }

    private fun startTimer() {
        song?.let {
            timer = Timer(it.playTime, it.isPlaying)
            timer.start()
        }
    }

    private fun restartTimer() {
        if (::timer.isInitialized) {
            timer.interrupt()
        }
        song?.second = 0
        binding.songStartTimeTv.text = "00:00"
        binding.songProgressSb.progress = 0

        timer = Timer(song!!.playTime, true)
        timer.start()
    }

    private fun formatTime(seconds: Int): String {
        val min = seconds / 60
        val sec = seconds % 60
        return String.format("%02d:%02d", min, sec)
    }

    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true) : Thread() {
        private var mills = 0f

        override fun run() {
            try {
                while (!isInterrupted) {
                    if (song == null) break
                    if (song!!.second >= playTime) break

                    if (isPlaying) {
                        sleep(50)
                        mills += 50

                        runOnUiThread {
                            binding.songProgressSb.progress = ((mills / 1000).toInt())
                        }

                        if (mills % 1000 == 0f) {
                            song!!.second++
                            runOnUiThread {
                                binding.songStartTimeTv.text = formatTime(song!!.second)
                            }
                        }
                    }
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::timer.isInitialized) {
            timer.interrupt()
        }
    }
}
