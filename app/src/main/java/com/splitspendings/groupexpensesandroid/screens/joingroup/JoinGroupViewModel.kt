package com.splitspendings.groupexpensesandroid.screens.joingroup

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

class JoinGroupViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(JoinGroupViewModel::class.java)) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return JoinGroupViewModel(application) as T
    }
}

const val INVITATION_CODE_SIZE = 8

class JoinGroupViewModel(
    val app: Application
) : AndroidViewModel(app) {

    private val groupsRepository = GroupRepository.getInstance()

    private val _eventNavigateToGroup = MutableLiveData<Long>()
    val eventNavigateToGroup: LiveData<Long>
        get() = _eventNavigateToGroup

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    val invitationCode = MutableLiveData<String>()

    val submitButtonEnabled = Transformations.map(invitationCode) {
        it?.let {
            it.matches(regex = Regex("[A-Za-z0-9]{$INVITATION_CODE_SIZE}"))
        }
    }

    val invitationCodeInputError = Transformations.map(invitationCode) {
        it?.let {
            when {
                !it.matches(regex = Regex("[A-Za-z0-9]*")) -> app.getString(R.string.must_contain_letters_and_digits_error)
                else -> null
            }
        }
    }

    init {
        _eventNavigateToGroup.value = null
        _status.value = Status(ApiStatus.DONE, null)
    }

    fun onSubmit() {
        submitCodeAndNavigateToGroup()
    }

    fun onEventNavigateToGroupComplete() {
        _eventNavigateToGroup.value = null
    }

    private fun submitCodeAndNavigateToGroup() {
        viewModelScope.launch {
            _status.value = Status(ApiStatus.LOADING, null)
            try {
                invitationCode.value?.let {
                    _eventNavigateToGroup.value = groupsRepository.joinGroupByCode(code = it)
                }
            } catch (e: Exception) {
                Timber.d("Failure: ${e.message}")
                _status.value = Status(ApiStatus.ERROR, app.getString(R.string.failed_join_group))
            }
        }
    }
}




