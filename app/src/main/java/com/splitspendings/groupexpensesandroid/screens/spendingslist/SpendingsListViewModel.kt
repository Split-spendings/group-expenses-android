package com.splitspendings.groupexpensesandroid.screens.spendingslist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.splitspendings.groupexpensesandroid.common.ApiStatus
import com.splitspendings.groupexpensesandroid.repository.GroupRepository
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
    application: Application
) : AndroidViewModel(application) {

    private val groupsRepository = GroupRepository.getInstance()

    val spendings = groupsRepository.getGroupSpendings(groupId)

    private val _apiStatus = MutableLiveData<ApiStatus>()
    val apiStatus: LiveData<ApiStatus>
        get() = _apiStatus

    private val _eventNavigateToSpending = MutableLiveData<Long>()
    val eventNavigateToSpending: LiveData<Long>
        get() = _eventNavigateToSpending

    private val _eventNavigateToNewSpending = MutableLiveData<Boolean>()
    val eventNavigateToNewSpending: LiveData<Boolean>
        get() = _eventNavigateToNewSpending

    private val _eventSuccessfulSpendingsUpload = MutableLiveData<Boolean>()
    val eventSuccessfulSpendingsUpload: LiveData<Boolean>
        get() = _eventSuccessfulSpendingsUpload

    init {
        _eventNavigateToSpending.value = null
        _eventNavigateToNewSpending.value = false
        _eventSuccessfulSpendingsUpload.value = false
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

    fun onEventSuccessfulSpendingsUploadComplete() {
        _eventSuccessfulSpendingsUpload.value = false
    }

    private fun loadGroupSpendings() {
        viewModelScope.launch {
            _apiStatus.value = ApiStatus.LOADING
            try {
                //TODO: add endpoint that accepts filtering (e.g. by currency)
                groupsRepository.refreshGroupSpendings(groupId)

                _apiStatus.value = ApiStatus.DONE

                _eventSuccessfulSpendingsUpload.value = true

            } catch (e: Exception) {
                Timber.d("Failure: ${e.message}")
                _apiStatus.value = ApiStatus.ERROR
            }
        }
    }
}