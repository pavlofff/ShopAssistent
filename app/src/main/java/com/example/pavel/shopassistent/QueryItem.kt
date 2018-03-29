package com.example.pavel.shopassistent

import android.arch.persistence.room.ColumnInfo

data class QueryItem(var id: Long = 0,
                     var price: Double = 0.0,
                     var count: Double = 0.0,
                     @ColumnInfo(name = "sum") var sum: Double = 0.0,
                     @ColumnInfo(name = "min_flag") var minFlag: Int = 0)