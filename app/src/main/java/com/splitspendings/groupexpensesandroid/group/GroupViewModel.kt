package com.splitspendings.groupexpensesandroid.group

import androidx.lifecycle.ViewModel
import timber.log.Timber

class GroupViewModel(var groupName: String) : ViewModel() {

    init {
        Timber.i("groupName: $groupName")
    }
}