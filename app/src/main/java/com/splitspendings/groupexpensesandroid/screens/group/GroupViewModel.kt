package com.splitspendings.groupexpensesandroid.screens.group

import android.app.Application
import androidx.lifecycle.*
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.common.formatGroup
import com.splitspendings.groupexpensesandroid.repository.dao.GroupDao
import com.splitspendings.groupexpensesandroid.repository.entities.Group
import kotlinx.coroutines.launch

class GroupViewModelFactory(
    private val groupId: Long,
    private val groupDao: GroupDao,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(GroupViewModel::class.java)) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return GroupViewModel(groupId, groupDao, application) as T
    }
}


class GroupViewModel(
    var groupId: Long,
    private val groupDao: GroupDao,
    application: Application
) : AndroidViewModel(application) {

    private val _counter = MutableLiveData<Int>()
    val counterString = Transformations.map(_counter) { counter ->
        "Counter: $counter"
    }

    private var _group = MutableLiveData<Group?>()
    var groupString = Transformations.map(_group) { group ->
        if (group != null) formatGroup(group, application.resources) else application.resources.getString(R.string.group_is_null)
    }

    var counterButtonEnabled = Transformations.map(_group) {
        it != null
    }

    init {
        _counter.value = 0
        initializeGroup()
    }

    fun onIncreaseCounter() {
        _counter.value = _counter.value?.inc()
    }

    private fun initializeGroup() {
        viewModelScope.launch {
            _group.value = getGroupFromRepository()
        }
    }

    private suspend fun getGroupFromRepository(): Group? {
        return groupDao.get(groupId)
    }
}