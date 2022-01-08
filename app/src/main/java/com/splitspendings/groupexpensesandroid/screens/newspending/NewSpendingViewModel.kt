package com.splitspendings.groupexpensesandroid.screens.newspending

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.splitspendings.groupexpensesandroid.common.Currency
import com.splitspendings.groupexpensesandroid.network.dto.NewSpendingDto
import com.splitspendings.groupexpensesandroid.repository.SpendingRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class NewSpendingViewModelFactory(
    private val groupId: Long,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(NewSpendingViewModel::class.java)) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return NewSpendingViewModel(groupId, application) as T
    }
}


class NewSpendingViewModel(
    private val groupId: Long,
    application: Application
) : AndroidViewModel(application) {

    private val spendingRepository = SpendingRepository.getInstance()

    private val _eventReset = MutableLiveData<Boolean>()
    val eventReset: LiveData<Boolean>
        get() = _eventReset

    private val _eventInvalidSpendingTitle = MutableLiveData<Boolean>()
    val eventInvalidSpendingTitle: LiveData<Boolean>
        get() = _eventInvalidSpendingTitle

    private val _eventNavigateToSpending = MutableLiveData<Long>()
    val eventNavigateToSpending: LiveData<Long>
        get() = _eventNavigateToSpending

    val spendingTitle = MutableLiveData<String>()

    val resetButtonEnabled = Transformations.map(spendingTitle) {
        it?.isNotEmpty()
    }

    init {
        _eventReset.value = false
        _eventNavigateToSpending.value = null
        _eventInvalidSpendingTitle.value = false
    }

    fun onReset() {
        _eventReset.value = true
    }

    fun onSubmit() {
        when {
            spendingTitle.value.isNullOrBlank() -> _eventInvalidSpendingTitle.value = true
            else -> {
                saveSpendingAndNavigateToSpending()
            }
        }
    }

    fun onEventResetComplete() {
        _eventReset.value = false
    }

    fun onEventInvalidSpendingTitleComplete() {
        _eventInvalidSpendingTitle.value = false
    }

    fun onEventNavigateToSpendingComplete() {
        _eventNavigateToSpending.value = null
    }

    private fun saveSpendingAndNavigateToSpending() {
        viewModelScope.launch {
            try {
                val newSpending = NewSpendingDto(
                    groupID = groupId,
                    title = spendingTitle.value!!,
                    timePayed = null,
                    currency = Currency.USD,
                    paidByGroupMembershipId = -1,
                    newItemDtoList = ArrayList()
                )
                _eventNavigateToSpending.value = spendingRepository.saveSpending(newSpending)
            } catch (e: Exception) {
                Timber.d("Failure: ${e.message}")
                // TODO add displaying some error status to user
            }
        }
    }
}
