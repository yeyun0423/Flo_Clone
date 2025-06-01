package com.example.flo.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.flo.data.User

@Dao
interface UserDao {
    @Insert
    fun insert(user: User): Long

    @Query("SELECT * FROM UserTable WHERE email = :email AND password = :password")
    fun getUser(email: String, password: String): User?

    @Query("SELECT * FROM UserTable WHERE email = :email")
    fun findByEmail(email: String): User?

    @Query("SELECT * FROM UserTable WHERE email = :email")
    fun getUserByEmail(email: String): User?
}