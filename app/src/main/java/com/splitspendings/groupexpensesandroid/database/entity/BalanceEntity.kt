package com.splitspendings.groupexpensesandroid.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.splitspendings.groupexpensesandroid.common.Currency
import com.splitspendings.groupexpensesandroid.model.AppUser
import com.splitspendings.groupexpensesandroid.model.Balance

@Entity(tableName = "balance")
data class BalanceEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    @ColumnInfo(name = "balance")
    val balance: String,

    @ColumnInfo(name = "currency")
    val currency: String,

    @ColumnInfo(name = "groupId")
    val groupId: Long,

    //TODO: RESEARCH how to use relations with live data
    @ColumnInfo(name = "withAppUserId")
    val withAppUserId: String,

    @ColumnInfo(name = "withLoginName")
    var withLoginName: String,

    @ColumnInfo(name = "withEmail")
    var withEmail: String,

    @ColumnInfo(name = "withFirstName")
    var withFirstName: String,

    @ColumnInfo(name = "withLastName")
    var withLastName: String
)

fun BalanceEntity.asModel() =
    Balance(
        id = id,
        balance = balance.toBigDecimal(),
        currency = Currency.valueOf(currency),
        groupId = groupId,
        withAppUser = AppUser(
            id = withAppUserId,
            loginName = withLoginName,
            email = withEmail,
            firstName = withFirstName,
            lastName = withLastName
        )
    )

fun List<BalanceEntity>.asModel() = map { it.asModel() }
