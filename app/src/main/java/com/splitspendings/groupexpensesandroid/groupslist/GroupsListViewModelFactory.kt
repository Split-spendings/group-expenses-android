package com.splitspendings.groupexpensesandroid.groupslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GroupsListViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(GroupsListViewModel::class.java)) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return GroupsListViewModel() as T
    }
}