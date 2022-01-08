package com.splitspendings.groupexpensesandroid.network.dto

data class GroupMembershipWithIdShortDto(
    val id: Long,
    val active: Boolean,
    val group: GroupDto,
    val appUser: AppUserDto
)