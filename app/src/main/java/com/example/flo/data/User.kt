package com.example.flo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserTable")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val email: String,
    val password: String
)