package com.splitspendings.groupexpensesandroid.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.splitspendings.groupexpensesandroid.model.AppUser

@Entity(tableName = "app_user")
data class AppUserEntity(

    @PrimaryKey
    var id: String,

    @ColumnInfo(name = "loginName")
    var loginName: String,

    @ColumnInfo(name = "email")
    var email: String,

    @ColumnInfo(name = "firstName")
    var firstName: String,

    @ColumnInfo(name = "lastName")
    var lastName: String
)

fun AppUserEntity.asModel() =
    AppUser(
        id = id,
        loginName = loginName,
        email = email,
        firstName = firstName,
        lastName = lastName
    )

fun List<AppUserEntity>.asModel() = map { it.asModel() }
