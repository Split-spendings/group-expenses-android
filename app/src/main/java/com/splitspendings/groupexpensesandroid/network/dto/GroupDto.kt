package com.splitspendings.groupexpensesandroid.network.dto

import com.splitspendings.groupexpensesandroid.repository.entity.Group

data class GroupDto(
    val id: Long,
    val name: String,
    val timeCreated: String,
    val lastTimeOpened: String,
    val lastTimeClosed: String?,
    val personal: Boolean
)

fun GroupDto.asModel(): Group = Group(name = name, personal = personal)

fun List<GroupDto>.asModel(): List<Group> = map { it.asModel() }
