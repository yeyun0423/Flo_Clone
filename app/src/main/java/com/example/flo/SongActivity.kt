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

        binding.songPreviousIv.setOnClickListener {
            changeSong(false)
        }

        binding.songNextIv.setOnClickListener {
            changeSong(true)
        }

        // 앨범 이미지 설정
        val resId = intent.getIntExtra("resId", R.drawable.img_album_exp2)
        binding.songAlbumIv.setImageResource(resId)

        initPlayer()
        startTimer()
        updateLikeUI()

        binding.songLikeIv.setOnClickListener {
            song?.let {
                it.isLike = !it.isLike
                SongDatabase.updateLikeStatus(it.id, it.isLike)
                updateLikeUI()
            }
        }
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
            timer = Timer(it.playTime, it.isPlaying, it.second)
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

        timer = Timer(song!!.playTime, true, 0)
        timer.start()

        song?.isPlaying = true
        setPlayerStatus(true)
    }

    private fun formatTime(seconds: Int): String {
        val min = seconds / 60
        val sec = seconds % 60
        return String.format("%02d:%02d", min, sec)
    }

    private fun updateLikeUI() {
        val isLiked = song?.isLike ?: false
        if (isLiked) {
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        } else {
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }
    }

    private fun changeSong(next: Boolean) {
        val songList = SongDatabase.getSongs()
        val currentSong = song
        if (currentSong == null || songList.isEmpty()) return

        val currentIndex = songList.indexOfFirst { it.id == currentSong.id }
        if (currentIndex == -1) return

        val newIndex = if (next) {
            (currentIndex + 1) % songList.size
        } else {
            if (currentIndex - 1 < 0) songList.size - 1 else currentIndex - 1
        }

        // 곡 변경
        song = songList[newIndex]
        SongDatabase.currentSong = song

        // 재생 상태 초기화
        song?.second = 0
        song?.isPlaying = true

        // 타이머 정지
        if (::timer.isInitialized) {
            timer.interrupt()
        }

        // UI 초기화
        binding.songStartTimeTv.text = "00:00"
        binding.songProgressSb.progress = 0
        binding.songProgressSb.max = song!!.playTime

        // 앨범 이미지도 같이 변경
        binding.songAlbumIv.setImageResource(song!!.imageResId)

        timer = Timer(song!!.playTime, true, 0)
        timer.start()

        initPlayer()
        setPlayerStatus(true)
        updateLikeUI()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::timer.isInitialized) {
            timer.interrupt()
        }
    }

    inner class Timer(
        private val playTime: Int,
        var isPlaying: Boolean = true,
        startSecond: Int = 0
    ) : Thread() {
        private var mills = (startSecond * 1000).toFloat()
        private var nextWholeSecond = startSecond + 1

        override fun run() {
            try {
                while (!isInterrupted) {
                    if (song == null) break
                    if (song!!.second >= playTime) break

                    if (isPlaying) {
                        sleep(50)
                        mills += 50

                        runOnUiThread {
                            binding.songProgressSb.progress = (mills / 1000).toInt()
                        }

                        if (mills >= nextWholeSecond * 1000) {
                            song!!.second++
                            nextWholeSecond++
                            runOnUiThread {
                                binding.songStartTimeTv.text = formatTime(song!!.second)
                            }
                        }
                    } else {
                        sleep(100)
                    }
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }
}
