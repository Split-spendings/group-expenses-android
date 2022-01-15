package com.splitspendings.groupexpensesandroid.network.dto

import com.splitspendings.groupexpensesandroid.common.Currency
import com.splitspendings.groupexpensesandroid.database.entity.SpendingEntity

data class SpendingShortDto(
    val id: Long,
    val title: String,
    // TODO: RESEARCH automatic converting to BigDecimal by API client
    val totalAmount: String,
    val currency: Currency,
    // TODO: RESEARCH automatic converting to ZonedDateTime by API client
    val timeCreated: String,
    val timePayed: String?,
    val addedByGroupMembership: GroupMemberDto,
    val paidByGroupMembership: GroupMemberDto
)

fun SpendingShortDto.asEntity(groupId: Long) =
    SpendingEntity(
        id = id,
        title = title,
        totalAmount = totalAmount,
        currency = currency.toString(),
        timeCreated = timeCreated,
        timePayed = timePayed,

        addedByAppUserId = addedByGroupMembership.appUser.id,
        addedByEmail = addedByGroupMembership.appUser.email,
        addedByLoginName = addedByGroupMembership.appUser.loginName,
        addedByFirstName = addedByGroupMembership.appUser.firstName,
        addedByLastName = addedByGroupMembership.appUser.lastName,

        paidByAppUserId = paidByGroupMembership.appUser.id,
        paidByEmail = paidByGroupMembership.appUser.email,
        paidByLoginName = paidByGroupMembership.appUser.loginName,
        paidByFirstName = paidByGroupMembership.appUser.firstName,
        paidByLastName = paidByGroupMembership.appUser.lastName,

        groupId = groupId
    )

fun List<SpendingShortDto>.asEntity(groupId: Long) = map { it.asEntity(groupId) }

fun List<SpendingShortDto>.groupMembers(): Set<GroupMemberDto> {
    val groupMembers = mutableSetOf<GroupMemberDto>()
    forEach {
        groupMembers.add(it.addedByGroupMembership)
        groupMembers.add(it.paidByGroupMembership)
    }
    return groupMembers
}