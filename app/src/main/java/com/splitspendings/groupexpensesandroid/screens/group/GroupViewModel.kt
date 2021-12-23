package com.splitspendings.groupexpensesandroid.screens.group

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.splitspendings.groupexpensesandroid.repository.GroupRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class GroupViewModelFactory(
    private val groupId: Long,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(GroupViewModel::class.java)) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return GroupViewModel(groupId, application) as T
    }
}


class GroupViewModel(
    val groupId: Long,
    application: Application
) : AndroidViewModel(application) {

    private val groupsRepository = GroupRepository.getInstance()

    val group = groupsRepository.getGroup(groupId)

    val spendings = groupsRepository.getGroupSpendings(groupId)

    private val _eventNavigateToSpending = MutableLiveData<Long>()
    val eventNavigateToSpending: LiveData<Long>
        get() = _eventNavigateToSpending

    init {
        _eventNavigateToSpending.value = null
        loadGroupSpendings()
    }

    fun onSpendingClicked(id: Long) {
        _eventNavigateToSpending.value = id
    }

    fun onEventNavigateToSpendingComplete() {
        _eventNavigateToSpending.value = null
    }

    private fun loadGroupSpendings() {
        viewModelScope.launch {
            try {
                //TODO: add endpoint that accepts filtering (e.g. by currency)
                groupsRepository.refreshGroupSpendings(groupId)
            } catch (e: Exception) {
                Timber.d("Failure: ${e.message}")
            }
        }
    }
}