package com.splitspendings.groupexpensesandroid.network.dto

import com.splitspendings.groupexpensesandroid.database.entity.GroupMemberEntity

data class GroupMemberDto(
    val id: Long,
    val active: Boolean,
    val appUser: AppUserDto
) {
    override fun equals(other: Any?) = other != null && other is GroupMemberDto && id == other.id
    override fun hashCode() = id.hashCode()
}

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

fun Iterable<GroupMemberDto>.asEntity(groupId: Long) = map { it.asEntity(groupId) }