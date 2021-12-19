package com.splitspendings.groupexpensesandroid.common

enum class ApiStatus { LOADING, ERROR, DONE }

enum class GroupsFilter(val value: String) {
    CURRENT("current"),
    FORMER("former"),
    ALL("all") }