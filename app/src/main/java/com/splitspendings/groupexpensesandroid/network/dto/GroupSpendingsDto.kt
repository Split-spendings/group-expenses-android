package com.splitspendings.groupexpensesandroid.network.dto

data class GroupSpendingsDto(
    val id: Long,
    val name: String,
    val spendings: List<SpendingShortDto>
)

