package com.splitspendings.groupexpensesandroid.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.splitspendings.groupexpensesandroid.database.GroupExpensesDatabase
import com.splitspendings.groupexpensesandroid.database.entity.asModel
import com.splitspendings.groupexpensesandroid.model.Group
import com.splitspendings.groupexpensesandroid.network.GroupExpensesApi
import com.splitspendings.groupexpensesandroid.network.dto.asEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class GroupRepository(private val database: GroupExpensesDatabase) {

    val groups: LiveData<List<Group>> = Transformations.map(database.groupDao.getAllLive()) {
        it.asModel()
    }

    suspend fun refreshGroups() {
        withContext(Dispatchers.IO) {
            Timber.d("refresh groups is called")
            val groups = GroupExpensesApi.retrofitService.appUserActiveGroups().groups
            database.groupDao.insertAll(groups.asEntity())
        }
    }
}