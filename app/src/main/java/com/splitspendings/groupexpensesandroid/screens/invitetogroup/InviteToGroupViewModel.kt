package com.splitspendings.groupexpensesandroid.screens.invitetogroup

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
    application: Application
) : AndroidViewModel(application) {

    private val groupsRepository = GroupRepository.getInstance()

    val group = groupsRepository.getGroup(groupId)

    private val _eventCopyCode = MutableLiveData<String>()
    val eventCopyCode: LiveData<String>
        get() = _eventCopyCode

    init {
        _eventCopyCode.value = null
        onRefreshInvitationCode()
    }

    fun onCopyCode() {
        _eventCopyCode.value = group.value?.invitationCode
    }

    fun onEventCopyCodeComplete() {
        _eventCopyCode.value = null
    }

    fun onRefreshInvitationCode() {
        viewModelScope.launch {
            try {
                groupsRepository.refreshInvitationCode(groupId = groupId)
            } catch (e: Exception) {
                Timber.d("Failure: ${e.message}")
                // TODO add displaying some error status to user
            }
        }
    }

    fun onGenerateNewCode() {
        viewModelScope.launch {
            try {
                groupsRepository.generateNewInvitationCode(groupId = groupId)
            } catch (e: Exception) {
                Timber.d("Failure: ${e.message}")
                // TODO add displaying some error status to user
            }
        }
    }
}




