package com.splitspendings.groupexpensesandroid.network.dto

import com.splitspendings.groupexpensesandroid.common.Currency

data class SpendingDto(
    val id: Long,
    val title: String,
    val totalAmount: String,
    val currency: Currency,
    val timeCreated: String,
    val timePayed: String?,
    val addedByGroupMembership: GroupMemberDto,
    val shares: List<ShareDto>
)