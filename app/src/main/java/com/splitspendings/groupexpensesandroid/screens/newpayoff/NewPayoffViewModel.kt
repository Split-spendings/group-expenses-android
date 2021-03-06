package com.splitspendings.groupexpensesandroid.screens.newpayoff

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
import com.splitspendings.groupexpensesandroid.common.GroupMembersFilter
import com.splitspendings.groupexpensesandroid.common.SUCCESS_STATUS_MILLISECONDS
import com.splitspendings.groupexpensesandroid.model.AppUser
import com.splitspendings.groupexpensesandroid.model.GroupMember
import com.splitspendings.groupexpensesandroid.model.Status
import com.splitspendings.groupexpensesandroid.network.dto.NewPayoffDto
import com.splitspendings.groupexpensesandroid.repository.BalanceRepository
import com.splitspendings.groupexpensesandroid.repository.CurrentAppUserRepository
import com.splitspendings.groupexpensesandroid.repository.GroupRepository
import com.splitspendings.groupexpensesandroid.repository.PayoffRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.math.BigDecimal

class NewPayoffViewModelFactory(
    private val groupId: Long,
    private val balanceId: Long?,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(NewPayoffViewModel::class.java)) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return NewPayoffViewModel(groupId, balanceId, application) as T
    }
}

const val MAX_PAYOFF_TITLE_SIZE = 100

class NewPayoffViewModel(
    val groupId: Long,
    balanceId: Long?,
    val app: Application
) : AndroidViewModel(app) {

    private val groupRepository = GroupRepository.getInstance()
    private val payoffRepository = PayoffRepository.getInstance()
    private val balanceRepository = BalanceRepository.getInstance()
    private val currentAppUserRepository = CurrentAppUserRepository.getInstance()

    val groupMembers = MutableLiveData<List<GroupMember>>()

    private val _eventNavigateToPayoff = MutableLiveData<Long>()
    val eventNavigateToPayoff: LiveData<Long>
        get() = _eventNavigateToPayoff

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    private val _submitStatus = MutableLiveData<Status>()
    val submitStatus: LiveData<Status>
        get() = _submitStatus

    val title = MutableLiveData<String>()
    val paidFor = MutableLiveData<GroupMember>()
    val paidTo = MutableLiveData<GroupMember>()
    val amount = MutableLiveData<BigDecimal>()
    val currency = MutableLiveData<Currency>()

    var paidForDefault: AppUser? = null
    var paidToDefault: AppUser? = null

    val paidForDefaultIndex = MutableLiveData<Int>()
    val paidToDefaultIndex = MutableLiveData<Int>()
    val amountDefault = MutableLiveData<BigDecimal>()
    val currencyDefaultIndex = MutableLiveData<Int>()

    val payoffTitleInputError = Transformations.map(title) {
        it?.let {
            when {
                it.isNotEmpty() && it.isBlank() -> app.getString(R.string.blank_error)
                else -> null
            }
        }
    }

    init {
        _eventNavigateToPayoff.value = null
        _status.value = Status(ApiStatus.DONE, null)
        _submitStatus.value = Status(ApiStatus.DONE, null)
        balanceId?.let { initDataFromBalance(it) } ?: run { initGroupMembers() }
    }

    private fun initGroupMembers() {
        viewModelScope.launch {
            groupMembers.value = groupRepository.getGroupMembers(groupId)
        }
    }

    private fun initDataFromBalance(balanceId: Long) {
        viewModelScope.launch {
            groupMembers.value = groupRepository.getGroupMembers(groupId)
            val balance = balanceRepository.getBalance(balanceId) ?: return@launch
            val currentAppUser = currentAppUserRepository.currentAppUser()
            val negativeBalance = balance.balance < BigDecimal.ZERO
            val paidForAppUser = if(negativeBalance) currentAppUser else balance.withAppUser
            val paidToAppUser = if(negativeBalance)  balance.withAppUser else currentAppUser
            val amount = if(negativeBalance) balance.balance.negate() else balance.balance
            paidForDefault = paidForAppUser
            paidToDefault = paidToAppUser
            paidForDefaultIndex.value = getGroupMemberIndex(paidForAppUser)
            paidToDefaultIndex.value = getGroupMemberIndex(paidToAppUser)
            amountDefault.value = amount
            currencyDefaultIndex.value = Currency.values().indexOf(balance.currency)
        }
    }

    private fun getGroupMemberIndex(groupMember: AppUser): Int? {
        val index = groupMembers.value?.let { groupMembers ->
            groupMembers.indexOfFirst { it.appUser.id == groupMember.id }
        }
        return if (index != -1) index else null
    }

    fun onTotalAmountChanged(newTotalAmount: BigDecimal) {
        amount.value = newTotalAmount
    }

    fun onSubmit() {
        savePayoffAndNavigateToPayoff()
    }

    fun onEventNavigateToPayoffComplete() {
        _eventNavigateToPayoff.value = null
    }

    fun onLoadGroupMembers() {
        viewModelScope.launch {
            _status.value = Status(ApiStatus.LOADING, null)
            try {
                groupRepository.refreshGroupMembers(groupId, GroupMembersFilter.ALL)

                groupMembers.value = groupRepository.getGroupMembers(groupId)

                paidForDefault?.let {
                    paidForDefaultIndex.value = getGroupMemberIndex(it)
                }
                paidToDefault?.let {
                    paidToDefaultIndex.value = getGroupMemberIndex(it)
                }

                _status.value = Status(ApiStatus.SUCCESS, app.getString(R.string.successful_group_members_upload))
                delay(SUCCESS_STATUS_MILLISECONDS)
                _status.value = Status(ApiStatus.DONE, null)

            } catch (e: Exception) {
                Timber.d("Failure: ${e.message}")
                _status.value = Status(ApiStatus.ERROR, app.getString(R.string.failed_group_members_upload))
            }
        }
    }

    private fun savePayoffAndNavigateToPayoff() {
        viewModelScope.launch {
            try {
                val title = title.value ?: return@launch
                val amount = amount.value ?: return@launch
                val paidFor = paidFor.value ?: return@launch
                val paidTo = paidTo.value ?: return@launch
                val currency = currency.value ?: return@launch

                _status.value = Status(ApiStatus.LOADING, null)
                _submitStatus.value = Status(ApiStatus.LOADING, null)
                val newPayoff = NewPayoffDto(
                    groupId = groupId,
                    title = title,
                    amount = amount.toString(),
                    currency = currency,
                    timePayed = null,
                    paidForAppUser = paidFor.appUser.id,
                    paidToAppUser = paidTo.appUser.id
                )
                _eventNavigateToPayoff.value = payoffRepository.savePayoff(newPayoff)
            } catch (e: Exception) {
                Timber.d("Failure: ${e.message}")
                _status.value = Status(ApiStatus.ERROR, app.getString(R.string.failed_save_new_payoff))
                _submitStatus.value = Status(ApiStatus.ERROR, app.getString(R.string.failed_save_new_payoff))
            }
        }
    }
}
