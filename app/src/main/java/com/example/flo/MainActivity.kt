package com.example.flo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivityMainBinding
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.util.Log



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    private var isPlaying = false
    private var currentSecond = 0
    private var totalDuration = 0

    private lateinit var db: AppDatabase
    private lateinit var albumDao: AlbumDao

    private val handler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            if (isPlaying) {
                if (currentSecond < totalDuration) {
                    updateSeekBar()
                    currentSecond++
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

        // jwt 확인
        val userId = SharedPrefsUtil.getJwt(this)
        if (userId == -1) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        // 로그인 되어있다면 메인 로직 계속 진행
        setTheme(R.style.Theme_FLO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getInstance(applicationContext)
        albumDao = db.albumDao()

        initBottomNavigation()
        insertDummySongs()
        initMiniplayer()
        initMiniplayerButtons()
        initBottomSheetLikeDeleteListener()

        val firstSong = SongDatabase.currentSong ?: SongDatabase.getSongs().firstOrNull()
        if (firstSong != null) {
            SongDatabase.currentSong = firstSong
            totalDuration = firstSong.playTime
        }
    }


    private fun initBottomSheetLikeDeleteListener() {
        val editBarLayout = findViewById<View>(R.id.editBarLayout)
        val deleteAllBtn = editBarLayout.findViewById<LinearLayout>(R.id.deleteAllBtn)

        deleteAllBtn.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    albumDao.updateAllIsLikeFalse()
                    val check = albumDao.getLikedAlbums()
                    Log.d("MainActivity", "삭제 후 DB liked album 수: ${check.size}")
                }

                val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_frm)
                val lockerFragment = navHostFragment?.childFragmentManager?.fragments
                    ?.find { it is LockerFragment } as? LockerFragment

                // 좋아요 프래그먼트를 완전히 새로 생성해서 붙이기
                lockerFragment?.childFragmentManager?.beginTransaction()
                    ?.replace(R.id.lockerContentFrame, MyLikeFragment())
                    ?.commitAllowingStateLoss()
            }
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
            val currentSong = SongDatabase.currentSong
            val albumImageResId = R.drawable.img_album_exp2

            if (currentSong == null) {
                val newSong = Song(
                    getSharedSongId(),
                    binding.mainMiniplayerTitleTv.text.toString(),
                    binding.mainMiniplayerSingerTv.text.toString(),
                    currentSecond,
                    totalDuration,
                    isPlaying,
                    "",
                    albumImageResId,
                    1,
                    false
                )
                SongDatabase.currentSong = newSong
                newSong.second = currentSecond
                newSong.isPlaying = isPlaying
            }

            saveSongId(SongDatabase.currentSong!!.id)

            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }

        binding.mainMiniplayerSb.max = totalDuration
        updateSeekBar()
    }

    private fun initMiniplayerButtons() {
        binding.mainPreviousBtn.setOnClickListener {
            changeSong(next = false)
        }

        binding.mainNextBtn.setOnClickListener {
            changeSong(next = true)
        }
    }

    private fun changeSong(next: Boolean) {
        val songList = SongDatabase.getSongs()
        val currentSong = SongDatabase.currentSong ?: return
        if (songList.isEmpty()) return

        val currentIndex = songList.indexOfFirst { it.id == currentSong.id }
        if (currentIndex == -1) return

        val newIndex = if (next) {
            (currentIndex + 1) % songList.size
        } else {
            if (currentIndex - 1 < 0) songList.size - 1 else currentIndex - 1
        }

        val newSong = songList[newIndex]
        SongDatabase.currentSong = newSong

        totalDuration = newSong.playTime
        currentSecond = 0
        isPlaying = true

        binding.mainMiniplayerTitleTv.text = newSong.title
        binding.mainMiniplayerSingerTv.text = newSong.singer
        binding.mainMiniplayerSb.max = totalDuration
        binding.mainMiniplayerSb.progress = 0

        saveSongId(newSong.id)

        togglePlayPauseUI()
        handler.removeCallbacks(updateRunnable)
        handler.post(updateRunnable)
        updateSeekBar()
    }

    private fun updateSeekBar() {
        binding.mainMiniplayerSb.progress = currentSecond
        binding.mainMiniplayerStartTv.text = formatTime(currentSecond)
        binding.mainMiniplayerEndTv.text = formatTime(totalDuration)

        SongDatabase.currentSong?.second = currentSecond
    }

    private fun togglePlayPauseUI() {
        if (isPlaying) {
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE
        } else {
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE
        }
    }

    private fun formatTime(seconds: Int): String {
        val min = seconds / 60
        val sec = seconds % 60
        return String.format("%02d:%02d", min, sec)
    }

    override fun onResume() {
        super.onResume()

        val savedId = getSharedSongId()
        val song = SongDatabase.getSong(savedId)
        if (song != null) {
            SongDatabase.currentSong = song
            binding.mainMiniplayerTitleTv.text = song.title
            binding.mainMiniplayerSingerTv.text = song.singer
        }

        currentSecond = SongDatabase.currentSong?.second ?: 0
        totalDuration = SongDatabase.currentSong?.playTime ?: 0
        isPlaying = SongDatabase.currentSong?.isPlaying ?: false
        updateSeekBar()
        togglePlayPauseUI()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateRunnable)
    }

    private fun insertDummySongs() {
        val dummySongs = listOf(
            Song(1, "사랑이라 했던 말 속에서", "캔트비블루", 0, 180, false, "music1.mp3", R.drawable.img_album_exp2, 1),
            Song(2, "somebody", "디오", 0, 190, false, "music2.mp3", R.drawable.img_album_exp3, 2),
            Song(3, "pain", "하현상", 0, 200, false, "music3.mp3", R.drawable.img_album_exp4, 3)
        )
        SongDatabase.addSongs(dummySongs)
    }

    private fun saveSongId(id: Int) {
        val pref = getSharedPreferences("song", MODE_PRIVATE)
        val editor = pref.edit()
        editor.putInt("songId", id)
        editor.apply()
    }

    private fun getSharedSongId(): Int {
        val pref = getSharedPreferences("song", MODE_PRIVATE)
        return pref.getInt("songId", -1)
    }

    fun updateMiniplayerUI() {
        val song = SongDatabase.currentSong
        if (song != null) {
            binding.mainMiniplayerTitleTv.text = song.title
            binding.mainMiniplayerSingerTv.text = song.singer
            currentSecond = song.second
            totalDuration = song.playTime
            isPlaying = song.isPlaying
            updateSeekBar()
            togglePlayPauseUI()

            if (isPlaying) {
                handler.removeCallbacks(updateRunnable)
                handler.post(updateRunnable)
            }
        }
    }
}
