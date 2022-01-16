package com.splitspendings.groupexpensesandroid.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.splitspendings.groupexpensesandroid.model.AppUser
import com.splitspendings.groupexpensesandroid.model.GroupMember

@Entity(tableName = "group_member")
data class GroupMemberEntity(

    @PrimaryKey
    var id: Long,

    @ColumnInfo(name = "groupId")
    val groupId: Long,

    @ColumnInfo(name = "active")
    val active: Boolean,

    @ColumnInfo(name = "appUserId")
    val appUserId: String,

    @ColumnInfo(name = "loginName")
    var loginName: String,

    @ColumnInfo(name = "email")
    var email: String,

    @ColumnInfo(name = "firstName")
    var firstName: String,

    @ColumnInfo(name = "lastName")
    var lastName: String
)

fun GroupMemberEntity.asModel() =
    GroupMember(
        id = id,
        groupId = groupId,
        active = active,
        appUser = AppUser(
            id = appUserId,
            loginName = loginName,
            email = email,
            firstName = firstName,
            lastName = lastName
        )
    )

fun List<GroupMemberEntity>.asModel() = map { it.asModel() }
