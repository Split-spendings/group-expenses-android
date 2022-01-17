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
    val addedByAppUser: AppUserDto,
    val paidForAppUser: AppUserDto,
    val paidToAppUser: AppUserDto,
)

fun PayoffDto.asEntity(groupId: Long) =
    PayoffEntity(
        id = id,
        title = title,
        amount = amount,
        currency = currency.toString(),
        timeCreated = timeCreated,
        timePayed = timePayed,

        addedByAppUserId = addedByAppUser.id,
        addedByEmail = addedByAppUser.email,
        addedByLoginName = addedByAppUser.loginName,
        addedByFirstName = addedByAppUser.firstName,
        addedByLastName = addedByAppUser.lastName,

        paidForAppUserId = paidForAppUser.id,
        paidForEmail = paidForAppUser.email,
        paidForLoginName = paidForAppUser.loginName,
        paidForFirstName = paidForAppUser.firstName,
        paidForLastName = paidForAppUser.lastName,

        paidToAppUserId = paidToAppUser.id,
        paidToEmail = paidToAppUser.email,
        paidToLoginName = paidToAppUser.loginName,
        paidToFirstName = paidToAppUser.firstName,
        paidToLastName = paidToAppUser.lastName,

        groupId = groupId
    )

fun List<PayoffDto>.asEntity(groupId: Long) = map { it.asEntity(groupId) }