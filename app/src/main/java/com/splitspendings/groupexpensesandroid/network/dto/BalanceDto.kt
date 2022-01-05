package com.splitspendings.groupexpensesandroid.network.dto

import com.splitspendings.groupexpensesandroid.common.Currency
import com.splitspendings.groupexpensesandroid.database.entity.BalanceEntity

data class BalanceDto(
    val id: Long,
    //TODO converter for BigDecimal
    val balance: String,
    val currency: Currency,
    val withAppUser: AppUserDto
)

fun BalanceDto.asEntity(groupId: Long): BalanceEntity {
    return BalanceEntity(
        id = id,
        balance = balance,
        currency = currency.toString(),
        groupId = groupId,

        withAppUserId = withAppUser.id,
        email = withAppUser.email,
        loginName = withAppUser.loginName,
        firstName = withAppUser.firstName,
        lastName = withAppUser.lastName
    )
}

fun List<BalanceDto>.asEntity(groupId: Long) = map { it.asEntity(groupId) }

