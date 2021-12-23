package com.splitspendings.groupexpensesandroid.screens.newgroup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.splitspendings.groupexpensesandroid.repository.GroupRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class NewGroupViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(NewGroupViewModel::class.java)) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return NewGroupViewModel(application) as T
    }
}


class NewGroupViewModel(application: Application) : AndroidViewModel(application) {

    private val groupsRepository = GroupRepository.getInstance()

    private val _eventReset = MutableLiveData<Boolean>()
    val eventReset: LiveData<Boolean>
        get() = _eventReset

    private val _eventNavigateToGroup = MutableLiveData<Long>()
    val eventNavigateToGroup: LiveData<Long>
        get() = _eventNavigateToGroup

    private val _eventInvalidGroupName = MutableLiveData<Boolean>()
    val eventInvalidGroupName: LiveData<Boolean>
        get() = _eventInvalidGroupName

    private val _usersToInvite = MutableLiveData<List<String>>()
    val usersToInvite: LiveData<List<String>>
        get() = _usersToInvite

    val groupName = MutableLiveData<String>()

    val resetButtonEnabled = Transformations.map(groupName) {
        it?.isNotEmpty()
    }

    init {
        _eventReset.value = false
        _eventNavigateToGroup.value = null
        _eventInvalidGroupName.value = false
        _usersToInvite.value = listOf("Harry", "Ron", "Hermione")
    }

    fun onReset() {
        _eventReset.value = true
    }

    fun onSubmit() {
        when {
            groupName.value.isNullOrBlank() -> _eventInvalidGroupName.value = true
            else -> {
                saveGroupAndNavigateToGroup()
            }
        }
    }

    fun onEventResetComplete() {
        _eventReset.value = false
    }

    fun onEventInvalidGroupNameComplete() {
        _eventInvalidGroupName.value = false
    }

    fun onEventNavigateToGroupComplete() {
        _eventNavigateToGroup.value = null
    }

    private fun saveGroupAndNavigateToGroup() {
        viewModelScope.launch {
            _eventNavigateToGroup.value = groupsRepository.saveGroup(name = groupName.value!!)
        }
    }

    fun onUserToInviteSelected(user: String, isChecked: Boolean) {
        Timber.d("user: $user - invite to group: $isChecked")
    }
}




