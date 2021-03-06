package com.splitspendings.groupexpensesandroid.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.splitspendings.groupexpensesandroid.model.Group

@Entity(tableName = "user_group")
data class GroupEntity(

    @PrimaryKey
    var id: Long,

    @ColumnInfo(name = "group_name")
    var name: String,

    @ColumnInfo(name = "current")
    var current: Boolean,

    @ColumnInfo(name = "invitation_code")
    var invitationCode: String?
)

fun GroupEntity.asModel() =
    Group(
        id = id,
        name = name,
        current = current,
        invitationCode = invitationCode
    )

fun List<GroupEntity>.asModel() = map { it.asModel() }
