package com.example.clowreed

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Clow::class], exportSchema = false, version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): Dao
}