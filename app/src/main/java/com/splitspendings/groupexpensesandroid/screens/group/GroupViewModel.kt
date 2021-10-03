package com.splitspendings.groupexpensesandroid.screens.group

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.splitspendings.groupexpensesandroid.repository.dao.GroupDao

class GroupViewModelFactory(
    private val groupId: Long,
    private val groupDao: GroupDao,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(GroupViewModel::class.java)) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return GroupViewModel(groupId, groupDao, application) as T
    }
}


class GroupViewModel(
    var groupId: Long,
    private val groupDao: GroupDao,
    application: Application
) : AndroidViewModel(application) {

    val group = groupDao.getLive(groupId)
}