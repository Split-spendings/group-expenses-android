package com.splitspendings.groupexpensesandroid.network.dto

import com.splitspendings.groupexpensesandroid.database.entity.GroupMemberEntity

data class GroupMemberDto(
    val id: Long,
    val active: Boolean,
    val appUser: AppUserDto
)

fun GroupMemberDto.asEntity(groupId: Long): GroupMemberEntity {
    return GroupMemberEntity(
        id = id,
        groupId = groupId,
        active = active,

        appUserId = appUser.id,
        email = appUser.email,
        loginName = appUser.loginName,
        firstName = appUser.firstName,
        lastName = appUser.lastName
    )
}

fun List<GroupMemberDto>.asEntity(groupId: Long) = map { it.asEntity(groupId) }