package com.splitspendings.groupexpensesandroid.network.dto

import com.splitspendings.groupexpensesandroid.database.entity.AppUserEntity

class AppUserDto(
    val id: String,
    val loginName: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val timeRegistered: String
)

fun AppUserDto.asEntity() =
    AppUserEntity(
        id = id,
        loginName = loginName,
        email = email,
        firstName = firstName,
        lastName = lastName
    )

fun List<AppUserDto>.asEntity() = map { it.asEntity() }