package com.splitspendings.groupexpensesandroid.repository

import android.content.Context
import androidx.lifecycle.Transformations
import com.splitspendings.groupexpensesandroid.database.GroupExpensesDatabase
import com.splitspendings.groupexpensesandroid.database.entity.asModel
import com.splitspendings.groupexpensesandroid.network.GroupExpensesApi
import com.splitspendings.groupexpensesandroid.network.dto.NewSpendingDto
import com.splitspendings.groupexpensesandroid.network.dto.asEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

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

    fun getSpendingShares(spendingId: Long) = Transformations
        .map(database.shareDao.getBySpendingIdLive(spendingId)) { it.asModel() }

    suspend fun refreshSpendingShares(spendingId: Long) {
        withContext(Dispatchers.IO) {
            //TODO
            delay(3000)
            /*val spendingShares = GroupExpensesApi.retrofitService.groupSpendings(groupId)
            database.shareDao.deleteBySpendingId(spendingId)
            database.spendingDao.insertAll(groupSpendings.spendings.asEntity(spendingId))*/
        }
    }

    suspend fun deleteSpending(spendingId: Long) {
        withContext(Dispatchers.IO) {
            //TODO
            delay(4000)
            /*GroupExpensesApi.retrofitService.leaveGroup(groupId)
                database.groupDao.updateCurrent(false, groupId)
                database.balanceDao.deleteByGroupId(groupId)
                database.groupMemberDao.deleteByGroupId(groupId)
                database.spendingDao.deleteByGroupId(groupId)*/
        }
    }
}