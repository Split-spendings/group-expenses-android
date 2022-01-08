package com.splitspendings.groupexpensesandroid.model

data class GroupMember (
    val id: Long,
    val groupId: Long,
    val active: Boolean,
    val appUSer: AppUser
)