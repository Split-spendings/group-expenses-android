package com.splitspendings.groupexpensesandroid.common.auth

import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.TokenResponse

/*
 * Wraps the AuthState class from the AppAuth library
 * Some or all of the auth state can be persisted to a secure location such as Encrypted Shared Preferences
 */
class AuthStateManager {

    companion object {
        @Volatile
        private var INSTANCE: AuthStateManager? = null

        fun getInstance(): AuthStateManager {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = AuthStateManager()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

    private var authState: AuthState? = null
    var idToken: String? = null

    /*
     * Manage storing or updating the token response
     */
    fun saveTokens(tokenResponse: TokenResponse) {
        // When refreshing tokens, the Identity Server does not issue a new ID token
        // The AppAuth code does not allow us to update the token response with the original ID token
        // Therefore we store the ID token separately
        if (tokenResponse.idToken != null) {
            this.idToken = tokenResponse.idToken
        }

        this.authState!!.update(tokenResponse, null)
    }

    /*
    * Clear tokens upon logout or when the session expires
    */
    fun clearTokens() {
        val metadata = this.authState?.authorizationServiceConfiguration
        this.authState = AuthState(metadata!!)
        this.idToken = null
    }

    var metadata: AuthorizationServiceConfiguration?
        get() {
            return this.authState?.authorizationServiceConfiguration
        }
        set(configuration) {
            this.authState = AuthState(configuration!!)
        }

    val tokenResponse: TokenResponse?
        get() {
            return this.authState?.lastTokenResponse
        }
}
