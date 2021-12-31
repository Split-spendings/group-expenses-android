package com.splitspendings.groupexpensesandroid.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.splitspendings.groupexpensesandroid.database.GroupExpensesDatabase
import com.splitspendings.groupexpensesandroid.database.entity.asModel
import com.splitspendings.groupexpensesandroid.model.Spending

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

    fun getSpending(id: Long): LiveData<Spending> = Transformations.map(database.spendingDao.getLive(id)) {
        it.asModel()
    }

    fun saveSpending(groupId: Long, spendingTitle: String) : Long {
        //TODO
        return -1
    }
}