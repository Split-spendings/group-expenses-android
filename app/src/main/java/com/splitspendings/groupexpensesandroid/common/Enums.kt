package com.splitspendings.groupexpensesandroid.common

enum class ApiStatus { LOADING, ERROR, DONE }

enum class GroupsFilter(val value: String) {
    CURRENT("current"),
    FORMER("former"),
    ALL("all")
}

enum class InviteOption {
    ALL_ACTIVE_MEMBERS,
    ADMINS_ONLY,
    OWNER_ONLY
}

enum class Currency {
    USD,
    EUR,
    GBP,
    PLN,
    UAH,
    RUB
}