package com.splitspendings.groupexpensesandroid.network.dto

import com.splitspendings.groupexpensesandroid.database.entity.GroupEntity

data class GroupDto(
    val id: Long,
    val name: String,
    val timeCreated: String,
    val lastTimeOpened: String,
    val lastTimeClosed: String?,
    val personal: Boolean
)

//TODO set current with from dto
fun GroupDto.asEntity(): GroupEntity = GroupEntity(name = name, current = true)

fun List<GroupDto>.asEntity(): List<GroupEntity> = map { it.asEntity() }
