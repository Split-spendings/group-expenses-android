package com.splitspendings.groupexpensesandroid.repository

import android.content.Context
import androidx.lifecycle.Transformations
import com.splitspendings.groupexpensesandroid.database.GroupExpensesDatabase
import com.splitspendings.groupexpensesandroid.database.entity.asModel
import com.splitspendings.groupexpensesandroid.network.GroupExpensesApi
import com.splitspendings.groupexpensesandroid.network.dto.NewPayoffDto
import com.splitspendings.groupexpensesandroid.network.dto.asEntity
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

    suspend fun savePayoff(newPayoff: NewPayoffDto): Long {
        val payoffSavedOnServer = GroupExpensesApi.retrofitService.createPayoff(newPayoff)
        return database.payoffDao.insert(payoffSavedOnServer.asEntity(newPayoff.groupId))
    }

    suspend fun deletePayoff(payoffId: Long) {
        withContext(Dispatchers.IO) {
            GroupExpensesApi.retrofitService.deletePayoff(payoffId)
            database.payoffDao.deleteById(payoffId)
        }
    }
}