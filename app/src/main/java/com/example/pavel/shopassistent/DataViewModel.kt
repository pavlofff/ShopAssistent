package com.example.pavel.shopassistent

import android.arch.lifecycle.LiveData
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.persistence.room.Room
import kotlin.concurrent.thread


class DataViewModel(application: Application) : AndroidViewModel(application) {

    val data: LiveData<List<QueryItem>>
    private val db: DB = Room.databaseBuilder<DB>(application, DB::class.java,"database").build()

    init {
        data = db.daoDB().getAllQueryItems()
    }

    fun getLiveDataItems(): LiveData<List<QueryItem>> {
        return data
    }

    fun insertItem(item: Item) {
       thread{db.daoDB().insertItem(item)}
    }

    fun deleteItem(id: Long){
        thread{db.daoDB().deleteItem(id)}
    }

    fun deleteAll(){
        thread{db.daoDB().deleteAll()}
    }
}