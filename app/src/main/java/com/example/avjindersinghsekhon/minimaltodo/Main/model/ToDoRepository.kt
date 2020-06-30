package com.example.avjindersinghsekhon.minimaltodo.Main.model

import com.example.avjindersinghsekhon.minimaltodo.Utility.ToDoItem
import com.example.avjindersinghsekhon.minimaltodo.database.dao.ToDoDao
import com.example.avjindersinghsekhon.minimaltodo.database.entity.ToDoEntity
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ToDoRepository(
        private val toDoDao: ToDoDao,
        private val ioThread: Scheduler = Schedulers.io()
) {
    private val disposables = CompositeDisposable()

    fun getItems() = toDoDao.getAll().map { ToDoEntity.toList(it) }

    fun saveItem(item: ToDoItem) {
        val disposable = Single.just(toDoDao)
                .subscribeOn(ioThread)
                .subscribe({
                    it.insertAll(ToDoEntity.fromItem(item))
                }, {/** Handle Error */})
        disposables.add(disposable)
    }

    fun deleteItem(item: ToDoItem) {
        val disposable = Single.just(toDoDao)
                .subscribeOn(ioThread)
                .subscribe({
                    it.delete(ToDoEntity.fromItem(item))
                }, {/** Handle Error */})
        disposables.add(disposable)
    }

    fun onCleared() {
        disposables.dispose()
    }
}