package com.splitspendings.groupexpensesandroid.network.dto

data class GroupMembersDto(
    val id: Long,
    val name : String,
    val members: List<GroupMemberDto>
)