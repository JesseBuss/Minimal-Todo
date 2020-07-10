package com.example.avjindersinghsekhon.minimaltodo.Main.model

import com.example.avjindersinghsekhon.minimaltodo.Utility.ToDoItem

interface ToDoListener {
    fun onClick(item: ToDoItem)
    fun onRemove(item: ToDoItem)
}