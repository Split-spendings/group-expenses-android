package com.splitspendings.groupexpensesandroid.model

import com.splitspendings.groupexpensesandroid.common.Currency
import java.math.BigDecimal
import java.time.ZonedDateTime

data class Spending(
    val id: Long,
    val title: String,
    val totalAmount: BigDecimal,
    val currency: Currency,
    val timeCreated: ZonedDateTime,
    val timePayed: ZonedDateTime?,
    val addedByGroupMembershipId: Long,
    val paidByGroupMembershipId: Long
)