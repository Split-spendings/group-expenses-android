package com.splitspendings.groupexpensesandroid.repository

import android.content.Context
import androidx.lifecycle.Transformations
import com.splitspendings.groupexpensesandroid.database.GroupExpensesDatabase
import com.splitspendings.groupexpensesandroid.database.entity.asModel
import com.splitspendings.groupexpensesandroid.network.GroupExpensesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PayoffRepository(private val database: GroupExpensesDatabase) {

    companion object {
        @Volatile
        private var INSTANCE: PayoffRepository? = null

        fun getInstance(): PayoffRepository {
            synchronized(this) {
                return INSTANCE!!
            }
        }

        fun init(context: Context) {
            synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = PayoffRepository(GroupExpensesDatabase.getInstance(context))
                }
            }
        }
    }

    fun getPayoff(payoffId: Long) = Transformations
        .map(database.payoffDao.getLive(payoffId)) { it.asModel() }

    //TODO
    /*suspend fun savePayoff(newSpending: NewSpendingDto): Long {
        val spendingSavedOnServer = GroupExpensesApi.retrofitService.createSpending(newSpending)
        return database.spendingDao.insert(spendingSavedOnServer.asEntity(newSpending.groupID))
    }*/

    suspend fun deletePayoff(payoffId: Long) {
        withContext(Dispatchers.IO) {
            GroupExpensesApi.retrofitService.deletePayoff(payoffId)
            database.payoffDao.deleteById(payoffId)
        }
    }
}