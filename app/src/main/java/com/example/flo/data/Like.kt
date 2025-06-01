package com.example.flo.data

import androidx.room.Entity

@Entity(tableName = "LikeTable", primaryKeys = ["userId", "albumId"])
data class Like(
    val userId: Int,
    val albumId: Int
)