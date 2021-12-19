package com.splitspendings.groupexpensesandroid.model

import com.splitspendings.groupexpensesandroid.common.Currency
import java.math.BigDecimal

data class Spending(
    val id: Long,
    val title: String,
    val totalAmount: BigDecimal,
    val currency: Currency
)