package com.splitspendings.groupexpensesandroid.network.dto

import com.splitspendings.groupexpensesandroid.common.Currency
import com.splitspendings.groupexpensesandroid.database.entity.BalanceEntity

data class BalanceDto(
    val id: Long,
    val balance: String,
    val currency: Currency,
    val withAppUser: AppUserDto
)

fun BalanceDto.asEntity(groupId: Long): BalanceEntity {
    return BalanceEntity(
        balance = balance,
        currency = currency.toString(),
        groupId = groupId,

        withAppUserId = withAppUser.id,
        withEmail = withAppUser.email,
        withLoginName = withAppUser.loginName,
        withFirstName = withAppUser.firstName,
        withLastName = withAppUser.lastName
    )
}

fun List<BalanceDto>.asEntity(groupId: Long) = map { it.asEntity(groupId) }

