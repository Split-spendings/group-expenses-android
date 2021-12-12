package com.splitspendings.groupexpensesandroid.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.splitspendings.groupexpensesandroid.model.Group

@Entity(tableName = "user_group")
data class GroupEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "group_name")
    var name: String,

    @ColumnInfo(name = "personal")
    var personal: Boolean
)

fun GroupEntity.asModel(): Group = Group(id = id, name = name, personal = personal)

fun List<GroupEntity>.asModel(): List<Group> = map { it.asModel() }
