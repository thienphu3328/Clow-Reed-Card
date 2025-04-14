package com.example.clowreed

import androidx.room.Dao
import androidx.room.Query
@Dao
interface Dao {
    @Query("SELECT*FROM sakuracards")
    fun getAll(): List<Clow>
}