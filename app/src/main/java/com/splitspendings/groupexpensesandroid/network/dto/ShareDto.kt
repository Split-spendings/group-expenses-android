package com.splitspendings.groupexpensesandroid.network.dto

import com.splitspendings.groupexpensesandroid.common.Currency
import com.splitspendings.groupexpensesandroid.database.entity.ShareEntity

data class ShareDto(
    val id: Long,
    val amount: String,
    val paidFor: AppUserDto
)

fun ShareDto.asEntity(spendingId: Long, currency: Currency) =
    ShareEntity(
        id = id,
        amount = amount,
        currency = currency.toString(),
        spendingId = spendingId,

        paidForEmail = paidFor.email,
        paidForAppUserId = paidFor.id,
        paidForLoginName = paidFor.loginName,
        paidForFirstName = paidFor.firstName,
        paidForLastName = paidFor.lastName
    )

fun List<ShareDto>.asEntity(spendingId: Long, currency: Currency) = map { it.asEntity(spendingId, currency) }
