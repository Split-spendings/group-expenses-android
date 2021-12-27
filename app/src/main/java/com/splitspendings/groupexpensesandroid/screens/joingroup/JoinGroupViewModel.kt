package com.splitspendings.groupexpensesandroid.screens.joingroup

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


class JoinGroupViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val groupsRepository = GroupRepository.getInstance()

    private val _eventReset = MutableLiveData<Boolean>()
    val eventReset: LiveData<Boolean>
        get() = _eventReset

    private val _eventNavigateToGroup = MutableLiveData<Long>()
    val eventNavigateToGroup: LiveData<Long>
        get() = _eventNavigateToGroup

    private val _eventInvalidCode = MutableLiveData<Boolean>()
    val eventInvalidCode: LiveData<Boolean>
        get() = _eventInvalidCode

    val code = MutableLiveData<String>()

    val resetButtonEnabled = Transformations.map(code) {
        it?.isNotEmpty()
    }

    init {
        _eventReset.value = false
        _eventNavigateToGroup.value = null
        _eventInvalidCode.value = false
    }

    fun onReset() {
        _eventReset.value = true
    }

    fun onSubmit() {
        when {
            code.value.isNullOrBlank() -> _eventInvalidCode.value = true
            else -> {
                saveGroupAndNavigateToGroup()
            }
        }
    }

    fun onEventResetComplete() {
        _eventReset.value = false
    }

    fun onEventInvalidCodeComplete() {
        _eventInvalidCode.value = false
    }

    fun onEventNavigateToGroupComplete() {
        _eventNavigateToGroup.value = null
    }

    private fun saveGroupAndNavigateToGroup() {
        viewModelScope.launch {
            try {
                _eventNavigateToGroup.value = groupsRepository.joinGroup(code = code.value!!)
            } catch (e: Exception) {
                Timber.d("Failure: ${e.message}")
                // TODO add displaying some error status to user
            }
        }
    }
}




