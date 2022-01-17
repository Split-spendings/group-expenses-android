package com.splitspendings.groupexpensesandroid.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.splitspendings.groupexpensesandroid.model.AppUser

@Entity(tableName = "current_app_user")
data class CurrentAppUserEntity(

    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "loginName")
    var loginName: String,

    @ColumnInfo(name = "email")
    var email: String,

    @ColumnInfo(name = "firstName")
    var firstName: String,

    @ColumnInfo(name = "lastName")
    var lastName: String
)

fun CurrentAppUserEntity.asModel() =
    AppUser(
        id = id,
        email = email,
        loginName = loginName,
        firstName = firstName,
        lastName = lastName
    )