package com.splitspendings.groupexpensesandroid.network.dto

data class GroupDto(
    val id: Long,
    val name: String,
    val timeCreated: String,
    val lastTimeOpened: String,
    val lastTimeClosed: String?,
    val personal: Boolean
)