package com.splitspendings.groupexpensesandroid.network.dto

import com.splitspendings.groupexpensesandroid.common.Currency
import com.splitspendings.groupexpensesandroid.database.entity.PayoffEntity

data class PayoffDto(
    val id: Long,
    val title: String,
    val amount: String,
    val currency: Currency,
    val timeCreated: String,
    val timePayed: String?,
    val addedBy: GroupMemberDto,
    val paidBy: GroupMemberDto,
    val paidTo: GroupMemberDto,
)

fun PayoffDto.asEntity(groupId: Long) =
    PayoffEntity(
        id = id,
        title = title,
        amount = amount,
        currency = currency.toString(),
        timeCreated = timeCreated,
        timePayed = timePayed,

        addedByAppUserId = addedBy.appUser.id,
        addedByEmail = addedBy.appUser.email,
        addedByLoginName = addedBy.appUser.loginName,
        addedByFirstName = addedBy.appUser.firstName,
        addedByLastName = addedBy.appUser.lastName,

        paidByAppUserId = paidBy.appUser.id,
        paidByEmail = paidBy.appUser.email,
        paidByLoginName = paidBy.appUser.loginName,
        paidByFirstName = paidBy.appUser.firstName,
        paidByLastName = paidBy.appUser.lastName,

        paidToAppUserId = paidTo.appUser.id,
        paidToEmail = paidTo.appUser.email,
        paidToLoginName = paidTo.appUser.loginName,
        paidToFirstName = paidTo.appUser.firstName,
        paidToLastName = paidTo.appUser.lastName,

        groupId = groupId
    )

fun List<PayoffDto>.asEntity(groupId: Long) = map { it.asEntity(groupId) }