package com.splitspendings.groupexpensesandroid.network.dto

import com.splitspendings.groupexpensesandroid.common.Currency
import com.splitspendings.groupexpensesandroid.database.entity.GroupEntity

data class GroupDto(
    val id: Long,
    val name: String,
    val timeCreated: String,
    val lastTimeOpened: String,
    val lastTimeClosed: String?,
    val personal: Boolean,
    val simplifyDebts: Boolean,
    val defaultCurrency: Currency,
    val isActiveMember: Boolean
)

fun GroupDto.asEntity() =
    GroupEntity(
        id = id,
        name = name,
        current = isActiveMember,
        invitationCode = null
    )

fun List<GroupDto>.asEntity() = map { it.asEntity() }
