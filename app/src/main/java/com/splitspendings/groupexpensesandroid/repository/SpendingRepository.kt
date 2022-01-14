package com.splitspendings.groupexpensesandroid.repository

import android.content.Context
import androidx.lifecycle.Transformations
import com.splitspendings.groupexpensesandroid.database.GroupExpensesDatabase
import com.splitspendings.groupexpensesandroid.database.entity.asModel
import com.splitspendings.groupexpensesandroid.network.GroupExpensesApi
import com.splitspendings.groupexpensesandroid.network.dto.NewSpendingDto
import com.splitspendings.groupexpensesandroid.network.dto.asEntity

class SpendingRepository(private val database: GroupExpensesDatabase) {

    companion object {
        @Volatile
        private var INSTANCE: SpendingRepository? = null

        fun getInstance(): SpendingRepository {
            synchronized(this) {
                return INSTANCE!!
            }
        }

        fun init(context: Context) {
            synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = SpendingRepository(GroupExpensesDatabase.getInstance(context))
                }
            }
        }
    }

    fun getSpending(id: Long) = Transformations
        .map(database.spendingDao.getLive(id)) { it.asModel() }

    suspend fun saveSpending(newSpending: NewSpendingDto): Long {
        val spendingSavedOnServer = GroupExpensesApi.retrofitService.createSpending(newSpending)
        return database.spendingDao.insert(spendingSavedOnServer.asEntity(newSpending.groupID))
    }
}