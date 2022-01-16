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

    @ColumnInfo(name = "timePayed")
    val timePayed: String?,

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
    @ColumnInfo(name = "paidByAppUserId")
    val paidByAppUserId: String,

    @ColumnInfo(name = "paidByLoginName")
    var paidByLoginName: String,

    @ColumnInfo(name = "paidByEmail")
    var paidByEmail: String,

    @ColumnInfo(name = "paidByFirstName")
    var paidByFirstName: String,

    @ColumnInfo(name = "paidByLastName")
    var paidByLastName: String,

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
        timePayed = timePayed?.let { ZonedDateTime.parse(timePayed) },
        addedBy = AppUser(
            id = addedByAppUserId,
            loginName = addedByLoginName,
            email = addedByEmail,
            firstName = addedByFirstName,
            lastName = addedByLastName
        ),
        paidBy = AppUser(
            id = paidByAppUserId,
            loginName = paidByLoginName,
            email = paidByEmail,
            firstName = paidByFirstName,
            lastName = paidByLastName
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
