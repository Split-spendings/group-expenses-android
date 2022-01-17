package com.splitspendings.groupexpensesandroid.model

import com.splitspendings.groupexpensesandroid.common.Currency
import java.math.BigDecimal
import java.time.ZonedDateTime

data class Payoff(
    val id: Long,
    val title: String,
    val amount: BigDecimal,
    val currency: Currency,
    val timeCreated: ZonedDateTime,
    val timePayed: ZonedDateTime?,
    val addedBy: AppUser,
    val paidFor: AppUser,
    val paidTo: AppUser,
    val groupId: Long
)