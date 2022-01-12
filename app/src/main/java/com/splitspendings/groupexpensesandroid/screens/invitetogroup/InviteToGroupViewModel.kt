package com.splitspendings.groupexpensesandroid.screens.invitetogroup

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
import com.splitspendings.groupexpensesandroid.common.SUCCESS_STATUS_MILLISECONDS
import com.splitspendings.groupexpensesandroid.model.Status
import com.splitspendings.groupexpensesandroid.repository.GroupRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class InviteToGroupViewModelFactory(
    private val groupId: Long,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(InviteToGroupViewModel::class.java)) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return InviteToGroupViewModel(groupId, application) as T
    }
}


class InviteToGroupViewModel(
    val groupId: Long,
    val app: Application
) : AndroidViewModel(app) {

    private val groupsRepository = GroupRepository.getInstance()

    val group = groupsRepository.getGroup(groupId)

    private val _eventCopyCode = MutableLiveData<String>()
    val eventCopyCode: LiveData<String>
        get() = _eventCopyCode

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    val copyButtonEnabled = Transformations.map(group) {
        group.value?.let {
            !it.invitationCode.isNullOrBlank()
        }
    }

    init {
        _eventCopyCode.value = null
        _status.value = Status(ApiStatus.DONE, null)
    }

    fun onCopyCode() {
        _eventCopyCode.value = group.value?.invitationCode
    }

    fun onEventCopyCodeComplete() {
        _eventCopyCode.value = null
    }

    fun onRefreshInvitationCode() {
        viewModelScope.launch {
            _status.value = Status(ApiStatus.LOADING, null)
            try {
                groupsRepository.refreshInvitationCode(groupId = groupId)

                _status.value = Status(ApiStatus.SUCCESS, app.getString(R.string.successful_invitation_code_upload))
                delay(SUCCESS_STATUS_MILLISECONDS)
                _status.value = Status(ApiStatus.DONE, null)

            } catch (e: Exception) {
                Timber.d("Failure: ${e.message}")
                _status.value = Status(ApiStatus.ERROR, app.getString(R.string.failed_get_invite_code))
            }
        }
    }

    fun onGenerateNewCode() {
        viewModelScope.launch {
            _status.value = Status(ApiStatus.LOADING, null)
            try {
                groupsRepository.generateNewInvitationCode(groupId = groupId)

                _status.value = Status(ApiStatus.SUCCESS, app.getString(R.string.successful_invitation_code_upload))
                delay(SUCCESS_STATUS_MILLISECONDS)
                _status.value = Status(ApiStatus.DONE, null)

            } catch (e: Exception) {
                Timber.d("Failure: ${e.message}")
                _status.value = Status(ApiStatus.ERROR, app.getString(R.string.failed_get_invite_code))
            }
        }
    }
}




