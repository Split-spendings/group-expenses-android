package com.splitspendings.groupexpensesandroid.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.splitspendings.groupexpensesandroid.database.GroupExpensesDatabase
import com.splitspendings.groupexpensesandroid.database.entity.GroupEntity
import com.splitspendings.groupexpensesandroid.database.entity.asModel
import com.splitspendings.groupexpensesandroid.model.Group
import com.splitspendings.groupexpensesandroid.network.GroupExpensesApi
import com.splitspendings.groupexpensesandroid.network.dto.asEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class GroupRepository(private val database: GroupExpensesDatabase) {

    companion object {
        @Volatile
        private var INSTANCE: GroupRepository? = null

        fun getInstance(): GroupRepository {
            synchronized(this) {
                return INSTANCE!!
            }
        }

        fun init(context: Context) {
            synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = GroupRepository(GroupExpensesDatabase.getInstance(context))
                }
            }
        }
    }

    val groups: LiveData<List<Group>> = Transformations.map(database.groupDao.getAllLive()) {
        it.asModel()
    }

    fun getGroup(id: Long): LiveData<Group> = Transformations.map(database.groupDao.getLive(id)) {
        it.asModel()
    }

    suspend fun refreshGroups() {
        withContext(Dispatchers.IO) {
            Timber.d("refresh groups is called")
            val groups = GroupExpensesApi.retrofitService.appUserActiveGroups().groups
            database.groupDao.clear()
            database.groupDao.insertAll(groups.asEntity())
        }
    }

    suspend fun saveGroup(name: String, personal: Boolean): Long {
        //TODO save to server
        return database.groupDao.insert(GroupEntity(name = name, personal = personal))
    }
}