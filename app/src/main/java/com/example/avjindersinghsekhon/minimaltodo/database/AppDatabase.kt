package com.example.avjindersinghsekhon.minimaltodo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.avjindersinghsekhon.minimaltodo.database.dao.ToDoDao
import com.example.avjindersinghsekhon.minimaltodo.database.entity.ToDoEntity

@Database(entities = [ToDoEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun toDoDao(): ToDoDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        @JvmStatic
        fun getAppDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "to-do").build()
            }
            return INSTANCE
        }
    }
}