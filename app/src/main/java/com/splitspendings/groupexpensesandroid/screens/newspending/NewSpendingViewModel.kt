package com.splitspendings.groupexpensesandroid.screens.newspending

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
import com.splitspendings.groupexpensesandroid.common.Currency
import com.splitspendings.groupexpensesandroid.common.SUCCESS_STATUS_MILLISECONDS
import com.splitspendings.groupexpensesandroid.model.GroupMember
import com.splitspendings.groupexpensesandroid.model.Status
import com.splitspendings.groupexpensesandroid.model.asDto
import com.splitspendings.groupexpensesandroid.model.asNewShare
import com.splitspendings.groupexpensesandroid.network.dto.NewItemDto
import com.splitspendings.groupexpensesandroid.network.dto.NewSpendingDto
import com.splitspendings.groupexpensesandroid.repository.GroupRepository
import com.splitspendings.groupexpensesandroid.repository.SpendingRepository
import kotlinx.coroutines.delay
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

const val MAX_SPENDING_TITLE_SIZE = 100

class NewSpendingViewModel(
    val groupId: Long,
    val app: Application
) : AndroidViewModel(app) {

    private val groupRepository = GroupRepository.getInstance()
    private val spendingRepository = SpendingRepository.getInstance()

    val groupMembers = groupRepository.getGroupMembersLive(groupId)

    private val _eventNavigateToSpending = MutableLiveData<Long>()
    val eventNavigateToSpending: LiveData<Long>
        get() = _eventNavigateToSpending

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    private val _submitStatus = MutableLiveData<Status>()
    val submitStatus: LiveData<Status>
        get() = _submitStatus

    val title = MutableLiveData<String>()
    val paidBy = MutableLiveData<GroupMember>()
    val totalAmount = MutableLiveData<BigDecimal>()
    val currency = MutableLiveData<Currency>()
    val equalSplit = MutableLiveData<Boolean>()
    val spendingNumberOfShares = MutableLiveData<Int>()
    val singleShareAmount = MutableLiveData<BigDecimal>()
    val newShares = Transformations.map(groupMembers) { it.asNewShare() }

    val titleInputError = Transformations.map(title) {
        it?.let {
            when {
                it.isNotEmpty() && it.isBlank() -> app.getString(R.string.blank_error)
                else -> null
            }
        }
    }

    init {
        _eventNavigateToSpending.value = null
        _status.value = Status(ApiStatus.DONE, null)
        _submitStatus.value = Status(ApiStatus.DONE, null)
    }

    fun onTotalAmountChanged(newTotalAmount: BigDecimal) {
        equalSplit.value?.let {
            if (it) {
                totalAmount.value = newTotalAmount
                calculateShares()
            }
        }
    }

    fun calculateShares() {
        newShares.value?.let { newShares ->
            val newTotalAmount = totalAmount.value
            newTotalAmount?.let {
                newShares.filter { share -> !share.hasShare }.forEach { share -> share.amount = BigDecimal.ZERO }

                val shares = newShares.filter { share -> share.hasShare }
                val numberOfShares = shares.count()
                spendingNumberOfShares.value = numberOfShares
                if (numberOfShares == 0) {
                    return
                }
                val newSingleShareAmount =
                    if (newTotalAmount == BigDecimal.ZERO) BigDecimal.ZERO
                    else newTotalAmount.divide(numberOfShares.toBigDecimal(), 2, BigDecimal.ROUND_HALF_EVEN)

                shares.forEach { share -> share.amount = newSingleShareAmount }
                if (shares.size > 1) {
                    shares[0].amount = newTotalAmount - newSingleShareAmount.multiply((shares.size - 1).toBigDecimal())
                }
                singleShareAmount.value = newSingleShareAmount
            }
        }
    }

    fun onShareChanged() {
        newShares.value?.let {
            totalAmount.value = it.sumOf { item -> item.amount }
        }
    }

    fun onSubmit() {
        saveSpendingAndNavigateToSpending()
    }

    fun onEventNavigateToSpendingComplete() {
        _eventNavigateToSpending.value = null
    }

    fun onLoadGroupMembers() {
        viewModelScope.launch {
            _status.value = Status(ApiStatus.LOADING, null)
            try {
                groupRepository.refreshGroupMembers(groupId)

                _status.value = Status(ApiStatus.SUCCESS, app.getString(R.string.successful_group_members_upload))
                delay(SUCCESS_STATUS_MILLISECONDS)
                _status.value = Status(ApiStatus.DONE, null)

            } catch (e: Exception) {
                Timber.d("Failure: ${e.message}")
                _status.value = Status(ApiStatus.ERROR, app.getString(R.string.failed_group_members_upload))
            }
        }
    }

    private fun saveSpendingAndNavigateToSpending() {
        viewModelScope.launch {
            val title = title.value ?: return@launch
            val shares = newShares.value ?: return@launch
            val paidBy = paidBy.value ?: return@launch

            _status.value = Status(ApiStatus.LOADING, null)
            _submitStatus.value = Status(ApiStatus.LOADING, null)
            try {
                val newSpending = NewSpendingDto(
                    groupID = groupId,
                    title = title,
                    timePayed = null,
                    currency = currency.value,
                    paidByGroupMembershipId = paidBy.id,
                    newItemDtoList = listOf(
                        NewItemDto(
                            title = title,
                            shares.filter { it.hasShare }.asDto()
                        )
                    )
                )
                _eventNavigateToSpending.value = spendingRepository.saveSpending(newSpending)
            } catch (e: Exception) {
                Timber.d("Failure: ${e.message}")
                _status.value = Status(ApiStatus.ERROR, app.getString(R.string.failed_save_new_spending))
                _submitStatus.value = Status(ApiStatus.ERROR, app.getString(R.string.failed_save_new_spending))
            }
        }
    }
}
