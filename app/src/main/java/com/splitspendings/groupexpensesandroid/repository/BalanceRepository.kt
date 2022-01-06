package com.splitspendings.groupexpensesandroid.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.splitspendings.groupexpensesandroid.database.GroupExpensesDatabase
import com.splitspendings.groupexpensesandroid.database.entity.asModel
import com.splitspendings.groupexpensesandroid.model.Balance

class BalanceRepository(private val database: GroupExpensesDatabase) {

    companion object {
        @Volatile
        private var INSTANCE: BalanceRepository? = null

        fun getInstance(): BalanceRepository {
            synchronized(this) {
                return INSTANCE!!
            }
        }

        fun init(context: Context) {
            synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = BalanceRepository(GroupExpensesDatabase.getInstance(context))
                }
            }
        }
    }

    fun getBalance(id: Long): LiveData<Balance> = Transformations.map(database.balanceDao.getLive(id)) {
        it.asModel()
    }
}