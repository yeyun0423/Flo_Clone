package com.example.flo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AlbumTable")
data class Album(
    @PrimaryKey val id: Int,
    var title: String = "",
    var singer: String = "",
    var coverImg: Int = 0,
    var isToggled: Boolean = false,
    var isLiked: Boolean = false,
    var isChecked: Boolean = false
)