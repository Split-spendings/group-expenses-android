package com.splitspendings.groupexpensesandroid.screens.spendingslist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.splitspendings.groupexpensesandroid.R
import com.splitspendings.groupexpensesandroid.common.ApiStatus
import com.splitspendings.groupexpensesandroid.model.Status
import com.splitspendings.groupexpensesandroid.repository.GroupRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class SpendingsListViewModelFactory(
    private val groupId: Long,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(SpendingsListViewModel::class.java)) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return SpendingsListViewModel(groupId, application) as T
    }
}


class SpendingsListViewModel(
    val groupId: Long,
    val app: Application
) : AndroidViewModel(app) {

    private val groupsRepository = GroupRepository.getInstance()

    val spendings = groupsRepository.getGroupSpendings(groupId)

    private val _eventNavigateToSpending = MutableLiveData<Long>()
    val eventNavigateToSpending: LiveData<Long>
        get() = _eventNavigateToSpending

    private val _eventNavigateToNewSpending = MutableLiveData<Boolean>()
    val eventNavigateToNewSpending: LiveData<Boolean>
        get() = _eventNavigateToNewSpending

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    init {
        _eventNavigateToSpending.value = null
        _eventNavigateToNewSpending.value = false
        loadGroupSpendings()
    }

    fun onSpendingClicked(id: Long) {
        _eventNavigateToSpending.value = id
    }

    fun onNewSpending() {
        _eventNavigateToNewSpending.value = true
    }

    fun onEventNavigateToSpendingComplete() {
        _eventNavigateToSpending.value = null
    }

    fun onEventNavigateToNewSpendingComplete() {
        _eventNavigateToNewSpending.value = false
    }

    private fun loadGroupSpendings() {
        viewModelScope.launch {
            _status.value = Status(ApiStatus.LOADING, null)
            try {
                //TODO: add endpoint that accepts filtering (e.g. by currency)
                groupsRepository.refreshGroupSpendings(groupId)

                _status.value = Status(ApiStatus.SUCCESS, app.getString(R.string.successful_spendings_upload))
                delay(3000)
                _status.value = Status(ApiStatus.DONE, null)

            } catch (e: Exception) {
                Timber.d("Failure: ${e.message}")
                _status.value = Status(ApiStatus.ERROR, app.getString(R.string.failed_spendings_upload))
            }
        }
    }
}