package com.splitspendings.groupexpensesandroid.network.dto

import com.splitspendings.groupexpensesandroid.common.Currency

data class NewSpendingDto(
    val groupID: Long,
    val title: String,
    val timePayed: String?,
    val currency: Currency?,
    val paidByGroupMembershipId: Long,
    val newItemDtoList: List<NewItemDto>
)

data class NewItemDto(
    val title: String,
    val newShareDtoList: List<NewShareDto>
)

data class NewShareDto(
    val amount: String,
    val paidForGroupMembershipId: Long
)