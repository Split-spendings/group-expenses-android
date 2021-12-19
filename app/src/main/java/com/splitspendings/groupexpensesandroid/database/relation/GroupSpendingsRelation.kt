package com.splitspendings.groupexpensesandroid.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.splitspendings.groupexpensesandroid.database.entity.GroupEntity
import com.splitspendings.groupexpensesandroid.database.entity.SpendingEntity

//TODO is it needed ???
data class GroupSpendingsRelation(
    @Embedded val group: GroupEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "groupId"
    )
    val spendings: List<SpendingEntity>
)