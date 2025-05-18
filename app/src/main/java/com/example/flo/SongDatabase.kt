package com.example.flo

//songDatabase.kt
object SongDatabase {

    private val songList = mutableListOf<Song>()
    var currentSong: Song? = null

    // 더미 곡 데이터를 songList에 추가하는 함수
    fun addSongs(songs: List<Song>) {
        if (songList.isEmpty()) {
            songList.addAll(songs)
        }
    }

    // 전체 곡 목록을 가져오는 함수
    fun getSongs(): List<Song> {
        return songList
    }

    // 특정 ID의 곡을 가져오는 함수
    fun getSong(id: Int): Song? {
        return songList.find { it.id == id }
    }

    // 특정 곡의 좋아요 상태를 토글하는 함수
    fun updateLikeStatus(id: Int, isLike: Boolean) {
        val song = getSong(id)
        if (song != null) {
            song.isLike = isLike
        }
    }

    // 좋아요한 곡 목록만 반환하는 함수
    fun getLikedSongs(): List<Song> {
        return songList.filter { it.isLike }
    }
    fun clearAllLikes() {
        songList.forEach { it.isLike = false }
    }
}
