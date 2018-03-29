package com.example.pavel.shopassistent

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [(Item::class)], version = 1, exportSchema = false)
abstract class DB : RoomDatabase() {

    abstract fun daoDB(): DaoDB
}