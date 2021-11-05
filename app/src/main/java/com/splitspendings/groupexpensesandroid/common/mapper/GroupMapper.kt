package com.splitspendings.groupexpensesandroid.common.mapper

import com.splitspendings.groupexpensesandroid.network.dto.GroupDto
import com.splitspendings.groupexpensesandroid.repository.model.Group

fun groupDtoToGroup(groupDto: GroupDto): Group =
    Group(name = groupDto.name, personal = groupDto.personal)

fun groupDtoListToGroupList(groupDtoList: List<GroupDto>): List<Group> =
    mapList(groupDtoList, ::groupDtoToGroup)