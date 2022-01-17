package com.splitspendings.groupexpensesandroid.repository

import android.content.Context
import androidx.lifecycle.Transformations
import com.splitspendings.groupexpensesandroid.database.GroupExpensesDatabase
import com.splitspendings.groupexpensesandroid.database.entity.asModel
import com.splitspendings.groupexpensesandroid.network.GroupExpensesApi
import com.splitspendings.groupexpensesandroid.network.dto.asEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CurrentAppUserRepository(private val database: GroupExpensesDatabase) {

    companion object {
        @Volatile
        private var INSTANCE: CurrentAppUserRepository? = null

        fun getInstance(): CurrentAppUserRepository {
            synchronized(this) {
                return INSTANCE!!
            }
        }

        fun init(context: Context) {
            synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = CurrentAppUserRepository(GroupExpensesDatabase.getInstance(context))
                }
            }
        }
    }

    val currentAppUser = Transformations
        .map(database.currentAppUserDao.getAllLive()) { it[0].asModel() }

    suspend fun refreshCurrentAppUser() {
        withContext(Dispatchers.IO) {
            val currentAppUser = GroupExpensesApi.retrofitService.profileShort()
            database.currentAppUserDao.clear()
            database.currentAppUserDao.insert(currentAppUser.asEntity())
        }
    }
}