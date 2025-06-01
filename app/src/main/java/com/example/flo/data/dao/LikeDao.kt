package com.example.flo.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flo.data.Like

@Dao
interface LikeDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    fun insertLike(like: Like)

    @Query("DELETE FROM LikeTable WHERE userId = :userId AND albumId = :albumId")
    fun deleteLike(userId: Int, albumId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM LikeTable WHERE userId = :userId AND albumId = :albumId)")
    fun isLiked(userId: Int, albumId: Int): Boolean

    @Query("SELECT albumId FROM LikeTable WHERE userId = :userId")
    fun getLikedAlbumIds(userId: Int): List<Int>
}