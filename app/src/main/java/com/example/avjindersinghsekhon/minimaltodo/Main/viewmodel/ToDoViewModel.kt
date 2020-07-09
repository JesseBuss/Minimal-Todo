package com.example.avjindersinghsekhon.minimaltodo.Main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.avjindersinghsekhon.minimaltodo.Main.model.ToDoRepository
import com.example.avjindersinghsekhon.minimaltodo.Utility.ToDoItem
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ToDoViewModel(
        private val repository: ToDoRepository,
        private val ioThread: Scheduler = Schedulers.io(),
        private val mainThread: Scheduler = AndroidSchedulers.mainThread()
): ViewModel() {

    val items = MutableLiveData<List<ToDoItem>>()
    private lateinit var disposable: Disposable

    init {
        getItems()
    }

    private fun getItems() {
        disposable = repository.getItems()
                .subscribeOn(ioThread)
                .observeOn(mainThread)
                .subscribe ({
                    items.value = it
                }, { /** Handle error */}
                )
    }

    fun saveItem(item: ToDoItem) {
        repository.saveItem(item)
    }

    fun deleteItem(item: ToDoItem) {
        repository.deleteItem(item)
    }

    override fun onCleared() {
        super.onCleared()

        disposable.dispose()
    }
}