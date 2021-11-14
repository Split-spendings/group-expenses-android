package com.splitspendings.groupexpensesandroid.common.auth

const val UNKNOWN_ERROR = "Unknown Error"

open class AuthException(
    val errorTitle: String,
    val errorDescription: String?
) : RuntimeException()

class AuthServerCommunicationException(
    errorTitle: String,
    errorDescription: String?
) : AuthException(errorTitle, errorDescription)

class InvalidIdTokenException(errorDescription: String) :
    AuthException("Invalid ID Token", errorDescription)

