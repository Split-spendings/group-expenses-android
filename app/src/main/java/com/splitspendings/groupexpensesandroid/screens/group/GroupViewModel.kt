package com.splitspendings.groupexpensesandroid.screens.group

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GroupViewModel(var groupName: String) : ViewModel() {

    private val _counter = MutableLiveData<Int>()
    val counterString = Transformations.map(_counter) { counter ->
        "Counter: $counter"
    }

    init {
        _counter.value = 0
    }

    fun onIncreaseCounter() {
        _counter.value = _counter.value?.inc()
    }
}


class GroupViewModelFactory(private val groupName: String) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(GroupViewModel::class.java)) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return GroupViewModel(groupName) as T
    }
}