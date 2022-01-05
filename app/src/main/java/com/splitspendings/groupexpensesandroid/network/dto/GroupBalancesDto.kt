package com.splitspendings.groupexpensesandroid.network.dto

data class GroupBalancesDto(
    val groupId: Long,
    val balances: List<BalanceDto>
)

