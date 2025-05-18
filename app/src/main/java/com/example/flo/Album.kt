package com.example.flo
import androidx.room.PrimaryKey
import androidx.room.Entity

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
