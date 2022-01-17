package com.splitspendings.groupexpensesandroid.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.splitspendings.groupexpensesandroid.common.Currency
import com.splitspendings.groupexpensesandroid.model.AppUser
import com.splitspendings.groupexpensesandroid.model.Payoff
import java.time.ZonedDateTime

@Entity(tableName = "payoff")
data class PayoffEntity(

    @PrimaryKey
    var id: Long,

    @ColumnInfo(name = "groupId")
    val groupId: Long,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "amount")
    val amount: String,

    @ColumnInfo(name = "currency")
    val currency: String,

    @ColumnInfo(name = "timeCreated")
    val timeCreated: String,

    //ADDED BY
    @ColumnInfo(name = "addedByAppUserId")
    val addedByAppUserId: String,

    @ColumnInfo(name = "addedByLoginName")
    var addedByLoginName: String,

    @ColumnInfo(name = "addedByEmail")
    var addedByEmail: String,

    @ColumnInfo(name = "addedByFirstName")
    var addedByFirstName: String,

    @ColumnInfo(name = "addedByLastName")
    var addedByLastName: String,

    //PAID BY
    @ColumnInfo(name = "paidForAppUserId")
    val paidForAppUserId: String,

    @ColumnInfo(name = "paidForLoginName")
    var paidForLoginName: String,

    @ColumnInfo(name = "paidForEmail")
    var paidForEmail: String,

    @ColumnInfo(name = "paidForFirstName")
    var paidForFirstName: String,

    @ColumnInfo(name = "paidForLastName")
    var paidForLastName: String,

    //PAID TO
    @ColumnInfo(name = "paidToAppUserId")
    val paidToAppUserId: String,

    @ColumnInfo(name = "paidToLoginName")
    var paidToLoginName: String,

    @ColumnInfo(name = "paidToEmail")
    var paidToEmail: String,

    @ColumnInfo(name = "paidToFirstName")
    var paidToFirstName: String,

    @ColumnInfo(name = "paidToLastName")
    var paidToLastName: String
)

fun PayoffEntity.asModel() =
    Payoff(
        id = id,
        title = title,
        amount = amount.toBigDecimal(),
        currency = Currency.valueOf(currency),
        timeCreated = ZonedDateTime.parse(timeCreated),
        addedBy = AppUser(
            id = addedByAppUserId,
            loginName = addedByLoginName,
            email = addedByEmail,
            firstName = addedByFirstName,
            lastName = addedByLastName
        ),
        paidFor = AppUser(
            id = paidForAppUserId,
            loginName = paidForLoginName,
            email = paidForEmail,
            firstName = paidForFirstName,
            lastName = paidForLastName
        ),
        paidTo = AppUser(
            id = paidToAppUserId,
            loginName = paidToLoginName,
            email = paidToEmail,
            firstName = paidToFirstName,
            lastName = paidToLastName
        ),
        groupId = groupId
    )

fun List<PayoffEntity>.asModel() = map { it.asModel() }
