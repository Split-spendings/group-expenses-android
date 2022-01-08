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
import com.splitspendings.groupexpensesandroid.model.GroupMember
import com.splitspendings.groupexpensesandroid.network.dto.NewSpendingDto
import com.splitspendings.groupexpensesandroid.repository.GroupRepository
import com.splitspendings.groupexpensesandroid.repository.SpendingRepository
import kotlinx.coroutines.launch
import timber.log.Timber
import java.math.BigDecimal

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

    private val groupRepository = GroupRepository.getInstance()
    private val spendingRepository = SpendingRepository.getInstance()

    val groupMembers = groupRepository.getGroupMembers(groupId)

    private val _eventReset = MutableLiveData<Boolean>()
    val eventReset: LiveData<Boolean>
        get() = _eventReset

    private val _eventInvalidSpendingTitle = MutableLiveData<Boolean>()
    val eventInvalidSpendingTitle: LiveData<Boolean>
        get() = _eventInvalidSpendingTitle

    private val _eventNavigateToSpending = MutableLiveData<Long>()
    val eventNavigateToSpending: LiveData<Long>
        get() = _eventNavigateToSpending

    val title = MutableLiveData<String>()
    val paidBy = MutableLiveData<GroupMember>()
    val totalAmount = MutableLiveData<BigDecimal>()
    val currency = MutableLiveData<Currency>()
    val equalSplit = MutableLiveData<Boolean>()

    val resetButtonEnabled = Transformations.map(title) {
        it?.isNotEmpty()
    }

    init {
        _eventReset.value = false
        _eventNavigateToSpending.value = null
        _eventInvalidSpendingTitle.value = false
        loadGroupMembers()
    }

    fun onReset() {
        _eventReset.value = true
    }

    fun onSubmit() {
        when {
            title.value.isNullOrBlank() -> _eventInvalidSpendingTitle.value = true
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

    private fun loadGroupMembers() {
        viewModelScope.launch {
            try {
                groupRepository.refreshGroupMembers(groupId)
                Timber.d("group members: ${groupMembers.value}")
            } catch (e: Exception) {
                Timber.d("Failure: ${e.message}")
                // TODO add displaying some error status to user
            }
        }
    }

    private fun saveSpendingAndNavigateToSpending() {
        viewModelScope.launch {
            try {
                val newSpending = NewSpendingDto(
                    groupID = groupId,
                    title = title.value!!,
                    timePayed = null,
                    currency = currency.value,
                    paidByGroupMembershipId = paidBy.value!!.id,
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
