package com.splitspendings.groupexpensesandroid.network.dto

import com.splitspendings.groupexpensesandroid.repository.entity.GroupEntity

data class GroupDto(
    val id: Long,
    val name: String,
    val timeCreated: String,
    val lastTimeOpened: String,
    val lastTimeClosed: String?,
    val personal: Boolean
)

fun GroupDto.asModel(): GroupEntity = GroupEntity(name = name, personal = personal)

fun List<GroupDto>.asModel(): List<GroupEntity> = map { it.asModel() }
