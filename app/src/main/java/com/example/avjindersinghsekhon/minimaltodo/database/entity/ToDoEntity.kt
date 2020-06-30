package com.example.avjindersinghsekhon.minimaltodo.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.avjindersinghsekhon.minimaltodo.Utility.ToDoItem
import java.util.*

@Entity(tableName = "to_do_entity")
data class ToDoEntity(
        @PrimaryKey val identifier: String,
        val text: String,
        @ColumnInfo(name = "has_reminder") val hasReminder: Boolean,
        val description: String,
        val color: Int,
        val date: Long
) {
    companion object {
        fun toList(list: List<ToDoEntity>): List<ToDoItem> {
            return list.map {
                ToDoItem(
                        it.identifier,
                        it.text,
                        it.description,
                        it.hasReminder,
                        if (it.date != null) Date(it.date) else null,
                        it.color
                )
            }
        }

        fun fromItem(item: ToDoItem) : ToDoEntity {
            return ToDoEntity(
                    identifier = item.identifier.toString(),
                    text = item.toDoText,
                    hasReminder =  item.hasReminder(),
                    description = item.getmToDoDescription(),
                    color = item.todoColor,
                    date = item.toDoDate.time
            )
        }
    }
}