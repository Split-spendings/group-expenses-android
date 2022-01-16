package com.splitspendings.groupexpensesandroid.network.dto

data class GroupPayoffsDto (
    val id: Long,
    val name : String,
    val payoffs: List<PayoffDto>
)