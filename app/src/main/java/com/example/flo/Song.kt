package com.example.flo

//song.kt
data class Song(
    var id: Int = 0,
    val title : String = "",
    val singer : String = "",
    var second: Int = 0,
    var playTime: Int = 0,
    var isPlaying: Boolean = false,
    var music: String = " ",
    val imageResId: Int= R.drawable.img_album_exp2,
    var albumIdx: Int,
    var isLike: Boolean = false

)

