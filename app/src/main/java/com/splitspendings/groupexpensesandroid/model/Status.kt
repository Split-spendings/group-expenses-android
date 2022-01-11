package com.splitspendings.groupexpensesandroid.model

import com.splitspendings.groupexpensesandroid.common.ApiStatus

data class Status(
    val apiStatus: ApiStatus,
    val message: String?
)