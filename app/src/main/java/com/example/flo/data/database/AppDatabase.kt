package com.example.flo.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.flo.data.Album
import com.example.flo.data.Like
import com.example.flo.data.User
import com.example.flo.data.dao.AlbumDao
import com.example.flo.data.dao.LikeDao
import com.example.flo.data.dao.UserDao

@Database(entities = [User::class, Album::class, Like::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun albumDao(): AlbumDao
    abstract fun likeDao(): LikeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "album-database"
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}