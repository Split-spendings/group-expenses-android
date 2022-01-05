package com.splitspendings.groupexpensesandroid.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.splitspendings.groupexpensesandroid.common.Currency
import com.splitspendings.groupexpensesandroid.model.AppUser
import com.splitspendings.groupexpensesandroid.model.Balance

@Entity(tableName = "balance")
data class BalanceEntity(

    @PrimaryKey
    var id: Long,

    // TODO: research other ways to store BigDecimal
    @ColumnInfo(name = "balance")
    val balance: String,

    // TODO: research other ways to store Enum
    @ColumnInfo(name = "currency")
    val currency: String,

    @ColumnInfo(name = "groupId")
    val groupId: Long,

    //TODO how to use relations with live data ???
    @ColumnInfo(name = "withAppUserId")
    val withAppUserId: String,

    @ColumnInfo(name = "loginName")
    var loginName: String,

    @ColumnInfo(name = "email")
    var email: String,

    @ColumnInfo(name = "firstName")
    var firstName: String,

    @ColumnInfo(name = "lastName")
    var lastName: String
)

//TODO remake with proper relation
fun BalanceEntity.asModel() =
    Balance(
        id = id,
        balance = balance.toBigDecimal(),
        currency = Currency.valueOf(currency),
        groupId = groupId,
        withAppUser = AppUser(
            id = withAppUserId,
            loginName = loginName,
            email = email,
            firstName = firstName,
            lastName = lastName
        )
    )

fun List<BalanceEntity>.asModel() = map { it.asModel() }
