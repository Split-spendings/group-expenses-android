package com.splitspendings.groupexpensesandroid.network.dto

import com.splitspendings.groupexpensesandroid.common.Currency

class NewPayoffDto (
    val groupId: Long,
    val title: String,
    val amount: String,
    val currency: Currency,
    val timePayed: String?,
    val paidForAppUser: String,
    val paidToAppUser: String
)