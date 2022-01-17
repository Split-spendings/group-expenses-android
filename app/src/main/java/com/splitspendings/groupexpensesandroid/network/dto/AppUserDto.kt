package com.splitspendings.groupexpensesandroid.network.dto

import com.splitspendings.groupexpensesandroid.database.entity.CurrentAppUserEntity

class AppUserDto(
    val id: String,
    val loginName: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val timeRegistered: String
)

fun AppUserDto.asEntity() =
    CurrentAppUserEntity(
        id = id,
        email = email,
        loginName = loginName,
        firstName = firstName,
        lastName = lastName
    )