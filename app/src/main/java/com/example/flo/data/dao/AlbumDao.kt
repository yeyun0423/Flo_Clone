package com.example.flo.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.flo.data.Album

@Dao
interface AlbumDao {

    @Insert
    fun insert(album: Album)

    @Query("SELECT * FROM AlbumTable")
    fun getAllAlbums(): List<Album>

    @Query("SELECT * FROM AlbumTable WHERE isLiked = 1")
    fun getLikedAlbums(): List<Album>

    @Query("SELECT COUNT(*) FROM AlbumTable")
    fun count(): Int

    @Query("UPDATE AlbumTable SET isLiked = :isLiked WHERE id = :albumId")
    fun updateLikeStatus(albumId: Int, isLiked: Boolean)

    @Query("UPDATE AlbumTable SET isLiked = 0")
    fun updateAllIsLikeFalse(): Int

    @Query("SELECT * FROM AlbumTable WHERE id = :albumId")
    fun getAlbumById(albumId: Int): Album?

}