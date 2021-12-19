package com.splitspendings.groupexpensesandroid.screens.group

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.splitspendings.groupexpensesandroid.database.GroupExpensesDatabase
import com.splitspendings.groupexpensesandroid.repository.GroupRepository

class GroupViewModelFactory(
    private val groupId: Long,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(GroupViewModel::class.java)) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return GroupViewModel(groupId, application) as T
    }
}


class GroupViewModel(
    groupId: Long,
    application: Application
) : AndroidViewModel(application) {

    private val groupsRepository = GroupRepository(GroupExpensesDatabase.getInstance(application))

    val group = groupsRepository.getGroup(groupId)
}