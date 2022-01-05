package com.splitspendings.groupexpensesandroid.model

import com.splitspendings.groupexpensesandroid.common.Currency
import java.math.BigDecimal

data class Balance(
    val id: Long,
    val balance: BigDecimal,
    val currency: Currency,
    val groupId: Long,
    val withAppUser: AppUser
)