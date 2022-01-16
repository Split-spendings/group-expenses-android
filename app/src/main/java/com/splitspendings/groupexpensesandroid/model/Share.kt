package com.splitspendings.groupexpensesandroid.model

import com.splitspendings.groupexpensesandroid.common.Currency
import java.math.BigDecimal

data class Share(
    val id: Long,
    val amount: BigDecimal,
    val currency: Currency,
    val paidFor: AppUser
)