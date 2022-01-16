package com.splitspendings.groupexpensesandroid.model

import java.math.BigDecimal

data class GroupMember(
    val id: Long,
    val groupId: Long,
    val active: Boolean,
    val appUser: AppUser
)

fun GroupMember.asNewShare() =
    NewShare(
        hasShare = true,
        paidFor = this,
        amount = BigDecimal.ZERO
    )

fun List<GroupMember>.asNewShare() = map { it.asNewShare() }