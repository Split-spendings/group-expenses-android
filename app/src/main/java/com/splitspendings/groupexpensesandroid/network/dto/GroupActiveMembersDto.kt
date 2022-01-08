package com.splitspendings.groupexpensesandroid.network.dto

data class GroupActiveMembersDto(
    val id: Long,
    val name : String,
    val members: List<GroupMemberDto>
)