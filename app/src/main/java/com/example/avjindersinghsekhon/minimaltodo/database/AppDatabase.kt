package com.example.avjindersinghsekhon.minimaltodo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.avjindersinghsekhon.minimaltodo.database.dao.ToDoDao
import com.example.avjindersinghsekhon.minimaltodo.database.entity.ToDoEntity

@Database(entities = [ToDoEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun toDoDao(): ToDoDao
}