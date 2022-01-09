package com.splitspendings.groupexpensesandroid.model

import com.splitspendings.groupexpensesandroid.network.dto.NewShareDto
import java.math.BigDecimal

data class NewShare(
    val hasShare: Boolean,
    val paidFor: GroupMember,
    val amount: BigDecimal
)

fun NewShare.asDto() = NewShareDto(
    amount = amount.toString(),
    paidForGroupMembershipId = paidFor.id
)

fun List<NewShare>.asDto() = map { it.asDto() }