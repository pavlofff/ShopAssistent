package com.example.pavel.shopassistent

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE

@Dao interface DaoDB {

    @Query("SELECT id, price, count, price/count AS sum, (SELECT MIN(price/count) FROM items) = price/count AS min_flag  FROM  items")
    fun getAllQueryItems(): LiveData<List<QueryItem>>

    @Query("SELECT * FROM items WHERE id = :id")
    fun getById(id: Long): Item

    @Query("DELETE FROM items")
    fun deleteAll()

    @Insert(onConflict = REPLACE)
    fun insertItem(item: Item)

    @Update(onConflict = REPLACE)
    fun updateItem(item: Item)

    @Query("DELETE FROM items WHERE id = :id")
    fun deleteItem(id: Long)
}