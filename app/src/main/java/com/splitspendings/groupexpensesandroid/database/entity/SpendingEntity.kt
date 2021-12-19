package com.splitspendings.groupexpensesandroid.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.splitspendings.groupexpensesandroid.common.Currency
import com.splitspendings.groupexpensesandroid.model.Spending

@Entity(tableName = "spending")
data class SpendingEntity(

    @PrimaryKey
    var id: Long,

    @ColumnInfo(name = "title")
    var title: String,

    // TODO: research other ways to store BigDecimal
    @ColumnInfo(name = "totalAmount")
    val totalAmount: String,

    // TODO: research other ways to store Enum
    @ColumnInfo(name = "currency")
    val currency: String,

    @ColumnInfo(name = "groupId")
    val groupId: Long
)

fun SpendingEntity.asModel() =
    Spending(
        id = id,
        title = title,
        totalAmount = totalAmount.toBigDecimal(),
        currency = Currency.valueOf(currency)
    )

fun List<SpendingEntity>.asModel() = map { it.asModel() }
