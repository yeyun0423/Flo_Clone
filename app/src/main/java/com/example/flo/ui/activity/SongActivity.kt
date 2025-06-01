package com.example.flo.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.R
import com.example.flo.data.Song
import com.example.flo.data.database.AppDatabase
import com.example.flo.data.database.SongDatabase
import com.example.flo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySongBinding
    private lateinit var timer: Timer
    private var song: Song? = null
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.Companion.getInstance(applicationContext)

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

        val resId = intent.getIntExtra("resId", R.drawable.img_album_exp2)
        binding.songAlbumIv.setImageResource(resId)

        initPlayer()
        startTimer()
        updateLikeUI()

        binding.songLikeIv.setOnClickListener {
            song?.let {
                it.isLike = !it.isLike

                // Firebase 저장 로직
                /*
                val firebase = com.google.firebase.database.FirebaseDatabase.getInstance()
                val likeRef = firebase.getReference("likes")
                val userId = "test_user"

                if (it.isLike) {
                    likeRef.child(userId).child(it.id.toString()).setValue(true)
                } else {
                    likeRef.child(userId).child(it.id.toString()).removeValue()
                }
                */

                // Room DB로 저장
                db.albumDao().updateLikeStatus(it.id, it.isLike)

                updateLikeUI()
                showLikeToast(it.isLike)
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

        song = songList[newIndex]
        SongDatabase.currentSong = song

        song?.second = 0
        song?.isPlaying = true

        if (::timer.isInitialized) {
            timer.interrupt()
        }

        binding.songStartTimeTv.text = "00:00"
        binding.songProgressSb.progress = 0
        binding.songProgressSb.max = song!!.playTime

        binding.songAlbumIv.setImageResource(song!!.imageResId)

        timer = Timer(song!!.playTime, true, 0)
        timer.start()

        initPlayer()
        setPlayerStatus(true)
        updateLikeUI()
    }

    private fun showLikeToast(isLiked: Boolean) {
        val layoutInflater = layoutInflater
        val view = layoutInflater.inflate(R.layout.toast_custom, null)

        val toastImage = view.findViewById<ImageView>(R.id.toast_iv)
        val toastText = view.findViewById<TextView>(R.id.toast_tv)

        if (isLiked) {
            toastImage.setImageResource(R.drawable.ic_my_like_on)
            toastText.text = "좋아요를 눌렀어요"
        } else {
            toastImage.setImageResource(R.drawable.ic_my_like_off)
            toastText.text = "좋아요를 취소했어요"
        }

        val toast = Toast(applicationContext)
        toast.view = view
        toast.duration = Toast.LENGTH_SHORT
        toast.show()
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
                        mills += 100f
                        val currentSecond = (mills / 1000).toInt()
                        song!!.second = currentSecond

                        if (currentSecond >= nextWholeSecond) {
                            nextWholeSecond++
                            runOnUiThread {
                                binding.songStartTimeTv.text = formatTime(currentSecond)
                                binding.songProgressSb.progress = currentSecond
                            }
                        }
                    }

                    sleep(100)
                }
            } catch (e: InterruptedException) {
                // 스레드 중단 시 예외 처리 없음
            }
        }
    }
}