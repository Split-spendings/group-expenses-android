package com.splitspendings.groupexpensesandroid.network.dto

import com.splitspendings.groupexpensesandroid.common.InviteOption

data class NewGroupDto(
    val name: String,
    val personal: Boolean,
    val simplifyDebts: Boolean,
    val inviteOption: InviteOption
)
