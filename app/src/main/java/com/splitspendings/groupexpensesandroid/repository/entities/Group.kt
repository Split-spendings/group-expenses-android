package com.splitspendings.groupexpensesandroid.repository.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_group")
data class Group(

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    @ColumnInfo(name = "group_name")
    var name: String
)