package com.splitspendings.groupexpensesandroid.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.splitspendings.groupexpensesandroid.common.Currency
import com.splitspendings.groupexpensesandroid.model.Spending
import java.time.ZonedDateTime

@Entity(tableName = "spending")
data class SpendingEntity(

    @PrimaryKey
    var id: Long,

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

    @ColumnInfo(name = "addedByGroupMembershipId")
    val addedByGroupMembershipId: Long,

    @ColumnInfo(name = "paidByGroupMembership")
    val paidByGroupMembershipId: Long,

    @ColumnInfo(name = "groupId")
    val groupId: Long
)

fun SpendingEntity.asModel() =
    Spending(
        id = id,
        title = title,
        totalAmount = totalAmount.toBigDecimal(),
        currency = Currency.valueOf(currency),
        timeCreated = ZonedDateTime.parse(timeCreated),
        timePayed = timePayed?.let { ZonedDateTime.parse(timePayed) },
        addedByGroupMembershipId,
        paidByGroupMembershipId
    )

fun List<SpendingEntity>.asModel() = map { it.asModel() }
