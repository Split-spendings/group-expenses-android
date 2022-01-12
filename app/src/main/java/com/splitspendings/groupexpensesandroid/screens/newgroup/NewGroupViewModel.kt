package com.splitspendings.groupexpensesandroid.screens.newgroup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.common.ApiStatus
import com.splitspendings.groupexpensesandroid.model.Status
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

const val MAX_GROUP_NAME_SIZE = 100

class NewGroupViewModel(
    val app: Application
) : AndroidViewModel(app) {

    private val groupsRepository = GroupRepository.getInstance()

    private val _eventNavigateToGroup = MutableLiveData<Long>()
    val eventNavigateToGroup: LiveData<Long>
        get() = _eventNavigateToGroup

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    //part of CHIP GROUP example
    /*private val _usersToInvite = MutableLiveData<List<String>>()
    val usersToInvite: LiveData<List<String>>
        get() = _usersToInvite*/

    val groupName = MutableLiveData<String>()

    val submitButtonEnabled = Transformations.map(groupName) {
        it?.let {
            it.isNotBlank() && it.length <= MAX_GROUP_NAME_SIZE
        }
    }

    val groupNameInputError = Transformations.map(groupName) {
        it?.let {
            when {
                it.isNotEmpty() && it.isBlank() -> app.getString(R.string.successful_spendings_upload)
                else -> null
            }
        }
    }

    init {
        _eventNavigateToGroup.value = null
        _status.value = Status(ApiStatus.DONE, null)

        //part of CHIP GROUP example
        //_usersToInvite.value = listOf("Harry", "Ron", "Hermione")
    }

    fun onSubmit() {
        saveGroupAndNavigateToGroup()
    }

    fun onEventNavigateToGroupComplete() {
        _eventNavigateToGroup.value = null
    }

    private fun saveGroupAndNavigateToGroup() {
        viewModelScope.launch {
            _status.value = Status(ApiStatus.LOADING, null)
            try {
                groupName.value?.let {
                    _eventNavigateToGroup.value = groupsRepository.saveGroup(name = it)
                }
            } catch (e: Exception) {
                Timber.d("Failure: ${e.message}")
                _status.value = Status(ApiStatus.ERROR, app.getString(R.string.failed_save_new_group))
            }
        }
    }

    //part of CHIP GROUP example
    /*fun onUserToInviteSelected(user: String, isChecked: Boolean) {
        Timber.d("user: $user - invite to group: $isChecked")
    }*/
}




