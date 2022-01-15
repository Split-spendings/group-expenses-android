package com.splitspendings.groupexpensesandroid.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.splitspendings.groupexpensesandroid.common.Currency
import com.splitspendings.groupexpensesandroid.model.AppUser
import com.splitspendings.groupexpensesandroid.model.Spending
import java.time.ZonedDateTime

@Entity(tableName = "spending")
data class SpendingEntity(

    @PrimaryKey
    var id: Long,

    @ColumnInfo(name = "groupId")
    val groupId: Long,

    @ColumnInfo(name = "title")
    var title: String,

    //TODO: RESEARCH other ways to store BigDecimal
    @ColumnInfo(name = "totalAmount")
    val totalAmount: String,

    //TODO: RESEARCH other ways to store Enum
    @ColumnInfo(name = "currency")
    val currency: String,

    //TODO: RESEARCH other ways to store ZonedDateTime
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
    var paidByLastName: String
)

fun SpendingEntity.asModel() =
    Spending(
        id = id,
        title = title,
        totalAmount = totalAmount.toBigDecimal(),
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
        )
    )

fun List<SpendingEntity>.asModel() = map { it.asModel() }
