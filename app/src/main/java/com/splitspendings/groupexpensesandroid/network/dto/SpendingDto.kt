package com.splitspendings.groupexpensesandroid.network.dto

import com.splitspendings.groupexpensesandroid.common.Currency
import com.splitspendings.groupexpensesandroid.database.entity.SpendingEntity

data class SpendingDto(
    val id: Long,
    val title: String,
    val timeCreated: String,
    val timePayed: String?,
    //TODO converter for BigDecimal
    val totalAmount: String,
    val currency: Currency,
    val addedByGroupMembership: GroupMembershipWithIdShortDto
)

fun SpendingDto.asEntity() =
    SpendingEntity(
        id = id,
        title = title,
        totalAmount = totalAmount,
        currency = currency.toString(),
        groupId = addedByGroupMembership.group.id
    )

fun List<SpendingDto>.asEntity() = map { it.asEntity() }