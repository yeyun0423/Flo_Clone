package com.example.flo

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {
    lateinit var binding: ActivitySongBinding           //나중에 초기화를 해줄게!! (var : 다시 초기화O  val :다시 초기화 X)
    private var mediaPlayer: MediaPlayer? = null  //activity 가 해제 될때 free 해줘야 하기 때문
    private lateinit var song: Song
    lateinit var timer: Timer


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

        initSong()
        setPlayer(song)
    }

    private fun initSong(){
        if(intent.hasExtra("title") && intent.hasExtra("singer")){
            song = Song(
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second",0),
                intent.getIntExtra("playTime",0),
                intent.getBooleanExtra("isPlaying",false)


            )
            startTimer()
        }

    }
    private fun setPlayer(song:Song){
        binding.songMusicTitleTv.text = intent.getStringExtra("title")!! // 텍스트 뷰를 바꿀건데 인텐트에서 타이틀이라는 키값을 가진 스트링으로 바꿔줄 것이다.
        binding.songSingerNameTv.text = intent.getStringExtra("singer")!!
        binding.songStartTimeTv.text=String.format("%02d:%02d",song.second/60,song.second % 60)
        binding.songEndTimeTv.text=String.format("%02d:%02d",song.second/60,song.playTime % 60)
        binding.songProgressSb.progress=(song.second*1000/song.playTime)
        setPlayerStatus(song.isPlaying)
    }
    fun setPlayerStatus(isPlaying: Boolean) {
        song.isPlaying=isPlaying
        timer.isPlaying=isPlaying
        if (isPlaying) {
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
        } else {
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
        }
    }
    private fun startTimer(){
        timer=Timer(song.playTime,song.isPlaying)
        timer.start()
    }

    inner class Timer(private val playTime:Int, var isPlaying: Boolean=true): Thread(){
        private var second: Int=0
        private var mills: Float=0f

        override fun run() {
            super.run()
            while (true){
                if(second >= playTime){
                    break
                }
                if(isPlaying){
                    sleep(50)
                    mills +=50

                    runOnUiThread{
                        binding.songProgressSb.progress = ((mills / playTime)*100).toInt()
                    }
                    if(mills % 1000 == 0f){
                        runOnUiThread{
                            binding.songStartTimeTv.text=String.format("%02d:%02d",second/60,second % 60)
                        }
                        second++
                    }
                }
            }
        }
    }

}
