package com.splitspendings.groupexpensesandroid.screens.balanceslist

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

class BalancesListViewModelFactory(
    private val groupId: Long,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(BalancesListViewModel::class.java)) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return BalancesListViewModel(groupId, application) as T
    }
}


class BalancesListViewModel(
    val groupId: Long,
    application: Application
) : AndroidViewModel(application) {

    private val groupsRepository = GroupRepository.getInstance()

    val balances = groupsRepository.getGroupBalances(groupId)

    private val _apiStatus = MutableLiveData<ApiStatus>()
    val apiStatus: LiveData<ApiStatus>
        get() = _apiStatus

    private val _eventNavigateToNewPayoff = MutableLiveData<Long>()
    val eventNavigateToNewPayoff: LiveData<Long>
        get() = _eventNavigateToNewPayoff

    private val _eventSuccessfulBalancesUpload = MutableLiveData<Boolean>()
    val eventSuccessfulBalancesUpload: LiveData<Boolean>
        get() = _eventSuccessfulBalancesUpload

    init {
        _eventNavigateToNewPayoff.value = null
        _eventSuccessfulBalancesUpload.value = false
        loadGroupBalances()
    }

    fun onNewPayoff(balanceId: Long) {
        _eventNavigateToNewPayoff.value = balanceId
    }

    fun onEventNavigateToNewPayoffComplete() {
        _eventNavigateToNewPayoff.value = null
    }

    fun onEventSuccessfulBalancesUploadComplete() {
        _eventSuccessfulBalancesUpload.value = false
    }

    private fun loadGroupBalances() {
        viewModelScope.launch {
            _apiStatus.value = ApiStatus.LOADING
            try {
                groupsRepository.refreshGroupBalances(groupId)

                _apiStatus.value = ApiStatus.DONE

                _eventSuccessfulBalancesUpload.value = true

            } catch (e: Exception) {
                Timber.d("Failure: ${e.message}")
                _apiStatus.value = ApiStatus.ERROR
            }
        }
    }
}