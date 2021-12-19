package com.splitspendings.groupexpensesandroid.network.dto

import com.splitspendings.groupexpensesandroid.common.Currency
import com.splitspendings.groupexpensesandroid.database.entity.SpendingEntity

data class SpendingShortDto(
    val id: Long,
    val title: String,
    //TODO converter for BigDecimal
    val totalAmount: String,
    val currency: Currency
)

fun SpendingShortDto.asEntity(groupId: Long) =
    SpendingEntity(
        id = id,
        title = title,
        totalAmount = totalAmount,
        currency = currency.toString(),
        groupId = groupId
    )

fun List<SpendingShortDto>.asEntity(groupId: Long) = map { it.asEntity(groupId) }