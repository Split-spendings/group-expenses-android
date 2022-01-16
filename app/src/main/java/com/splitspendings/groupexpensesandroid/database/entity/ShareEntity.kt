package com.splitspendings.groupexpensesandroid.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.splitspendings.groupexpensesandroid.common.Currency
import com.splitspendings.groupexpensesandroid.model.AppUser
import com.splitspendings.groupexpensesandroid.model.Share

@Entity(tableName = "spending_share")
data class ShareEntity(

    @PrimaryKey
    var id: Long,

    @ColumnInfo(name = "spendingId")
    val spendingId: Long,

    @ColumnInfo(name = "amount")
    val amount: String,

    @ColumnInfo(name = "currency")
    val currency: String,

    //PAID FOR
    @ColumnInfo(name = "paidForAppUserId")
    val paidForAppUserId: String,

    @ColumnInfo(name = "paidForLoginName")
    var paidForLoginName: String,

    @ColumnInfo(name = "paidForEmail")
    var paidForEmail: String,

    @ColumnInfo(name = "paidForFirstName")
    var paidForFirstName: String,

    @ColumnInfo(name = "paidForLastName")
    var paidForLastName: String
)

fun ShareEntity.asModel() =
    Share(
        id = id,
        amount = amount.toBigDecimal(),
        currency = Currency.valueOf(currency),
        paidFor = AppUser(
            id = paidForAppUserId,
            loginName = paidForLoginName,
            email = paidForEmail,
            firstName = paidForFirstName,
            lastName = paidForLastName
        )
    )

fun List<ShareEntity>.asModel() = map { it.asModel() }
