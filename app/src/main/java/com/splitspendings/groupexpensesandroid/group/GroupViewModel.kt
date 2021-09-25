package com.splitspendings.groupexpensesandroid.group

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

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