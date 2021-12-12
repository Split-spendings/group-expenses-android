package com.splitspendings.groupexpensesandroid.repository.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_group")
data class Group(

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "group_name")
    var name: String,

    @ColumnInfo(name = "personal")
    var personal: Boolean
)
