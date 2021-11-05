package com.splitspendings.groupexpensesandroid.common

enum class ApiStatus { LOADING, ERROR, DONE }

enum class GroupsFilter(val value: String) {
    PERSONAL("personal"),
    NOT_PERSONAL("not_personal"),
    ALL("all") }