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
    private var totalDuration = 0

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
        setTheme(R.style.Theme_FLO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomNavigation()
        insertDummySongs()

        // 첫 곡 세팅
        val firstSong = SongDatabase.currentSong ?: SongDatabase.getSongs().firstOrNull()
        if (firstSong != null) {
            SongDatabase.currentSong = firstSong
            totalDuration = firstSong.playTime
        }

        initMiniplayer()
        initMiniplayerButtons()
    }

    // 하단 탭 초기화
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

    // 미니플레이어 초기화
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
                    albumImageResId
                )
                SongDatabase.currentSong = newSong
            } else {
                currentSong.second = currentSecond
                currentSong.isPlaying = isPlaying
            }

            saveSongId(SongDatabase.currentSong!!.id)

            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }

        binding.mainMiniplayerSb.max = totalDuration
        updateSeekBar()
    }

    // 이전, 다음 버튼 클릭 리스너 설정
    private fun initMiniplayerButtons() {
        binding.mainPreviousBtn.setOnClickListener {
            changeSong(next = false)
        }

        binding.mainNextBtn.setOnClickListener {
            changeSong(next = true)
        }
    }

    // 이전/다음 곡 변경 함수
    private fun changeSong(next: Boolean) {
        val songList = SongDatabase.getSongs()
        val currentSong = SongDatabase.currentSong
        if (currentSong == null || songList.isEmpty()) return

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

    // SeekBar 및 시간 텍스트 업데이트
    private fun updateSeekBar() {
        binding.mainMiniplayerSb.progress = currentSecond
        binding.mainMiniplayerStartTv.text = formatTime(currentSecond)
        binding.mainMiniplayerEndTv.text = formatTime(totalDuration)

        SongDatabase.currentSong?.second = currentSecond
    }

    // 재생/일시정지 버튼 UI 토글
    private fun togglePlayPauseUI() {
        if (isPlaying) {
            binding.mainMiniplayerBtn.visibility = android.view.View.GONE
            binding.mainPauseBtn.visibility = android.view.View.VISIBLE
        } else {
            binding.mainMiniplayerBtn.visibility = android.view.View.VISIBLE
            binding.mainPauseBtn.visibility = android.view.View.GONE
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

    // 더미 곡 데이터 추가
    private fun insertDummySongs() {
        val dummySongs = listOf(
            Song(1, "사랑이라 했던 말 속에서", "캔트비블루", 0, 180, false, "music1.mp3", R.drawable.img_album_exp2),
            Song(2, "somebody", "디오", 0, 200, false, "music2.mp3", R.drawable.img_album_exp3),
            Song(3, "pain", "하현상", 0, 240, false, "music3.mp3", R.drawable.img_album_exp4),
            Song(4, "O", "코드 쿤스트", 0, 180, false, "music4.mp3", R.drawable.img_album_exp5),
            Song(5, "Cruise", "BOYCOLD", 0, 200, false, "music5.mp3", R.drawable.img_album_exp6),
            Song(6, "55", "코드 쿤스트", 0, 210, false, "music6.mp3", R.drawable.img_album_exp),
            Song(7, "다정히 내 이름을 부르면", "경서예지", 0, 230, false, "music7.mp3", R.drawable.img_album_exp2),
            Song(8, "한 페이지가 될 수 있게", "DAY6", 0, 220, false, "music8.mp3", R.drawable.img_album_exp3),
            Song(9, "좋아좋아", "조정석", 0, 190, false, "music9.mp3", R.drawable.img_album_exp4),
            Song(10, "비와 당신", "이무진", 0, 205, false, "music10.mp3", R.drawable.img_album_exp5),
            Song(11, "모든 날, 모든 순간", "폴킴", 0, 215, false, "music11.mp3", R.drawable.img_album_exp6),
            Song(12, "헤어지자 말해요", "박재정", 0, 225, false, "music12.mp3", R.drawable.img_album_exp3),
            Song(13, "LOVE me", "BE'O", 0, 200, false, "music13.mp3", R.drawable.img_album_exp4),
            Song(14, "나의 X에게", "경서", 0, 210, false, "music14.mp3", R.drawable.img_album_exp6)
        )
        SongDatabase.addSongs(dummySongs)
    }



    // SharedPreferences에 곡 ID 저장
    private fun saveSongId(id: Int) {
        val pref = getSharedPreferences("song", MODE_PRIVATE)
        val editor = pref.edit()
        editor.putInt("songId", id)
        editor.apply()
    }

    // SharedPreferences에서 곡 ID 불러오기
    private fun getSharedSongId(): Int {
        val pref = getSharedPreferences("song", MODE_PRIVATE)
        return pref.getInt("songId", -1)
    }

    // 외부에서 UI 갱신용 함수
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
