package com.example.flo

import androidx.room.*

@Dao
interface LikeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLike(like: Like)

    @Query("DELETE FROM LikeTable WHERE userId = :userId AND albumId = :albumId")
    fun deleteLike(userId: Int, albumId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM LikeTable WHERE userId = :userId AND albumId = :albumId)")
    fun isLiked(userId: Int, albumId: Int): Boolean

    @Query("SELECT albumId FROM LikeTable WHERE userId = :userId")
    fun getLikedAlbumIds(userId: Int): List<Int>
}
