package com.splitspendings.groupexpensesandroid.common.auth

import android.net.Uri

/*
 * Standard mobile OAuth configuration settings
 */
class AuthConfig {

    lateinit var issuer: String
    lateinit var clientID: String
    lateinit var redirectUri: String
    lateinit var postLogoutRedirectUri: String
    lateinit var scope: String

    fun getIssuerUri(): Uri = Uri.parse(issuer)

    fun getRedirectUri(): Uri = Uri.parse(redirectUri)

    fun getPostLogoutRedirectUri(): Uri = Uri.parse(postLogoutRedirectUri)
}
