package com.example.avjindersinghsekhon.minimaltodo.Main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.avjindersinghsekhon.minimaltodo.Main.model.ToDoRepository
import com.example.avjindersinghsekhon.minimaltodo.database.dao.ToDoDao

class ToDoViewModelFactory(
        private val dao: ToDoDao
) :ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val repository = ToDoRepository(dao)
        return ToDoViewModel(repository) as T
    }
}