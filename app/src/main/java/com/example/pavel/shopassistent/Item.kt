package com.example.pavel.shopassistent

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "items")
data class Item(var price: Double = 0.0,
                var count: Double = 0.0) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}