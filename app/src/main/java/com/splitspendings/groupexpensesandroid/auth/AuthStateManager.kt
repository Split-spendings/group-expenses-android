package com.splitspendings.groupexpensesandroid.auth

import android.content.SharedPreferences
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.TokenResponse
import timber.log.Timber

/*
 * Wraps the AuthState class from the AppAuth library
 * Some or all of the auth state can be persisted to a secure location such as Encrypted Shared Preferences
 */
class AuthStateManager(private var authPrefs: SharedPreferences) {

    companion object {
        @Volatile
        private var INSTANCE: AuthStateManager? = null

        fun getInstance(): AuthStateManager {
            synchronized(this) {
                return INSTANCE!!
            }
        }

        fun init(authPrefs: SharedPreferences) {
            val newInstance = AuthStateManager(authPrefs)
            try {

                val authStateJson = authPrefs.getString("stateJson", null)
                authStateJson?.let {
                    Timber.d("deserialize authStateJson: $authStateJson")
                    newInstance.authState = AuthState.jsonDeserialize(authStateJson)
                }

                val idToken = authPrefs.getString("idToken", null)
                idToken?.let {
                    newInstance.idToken = idToken
                    Timber.d("deserialize idToken: $idToken")
                }

            } catch (ex: Exception) {
                Timber.e(ex)
            }
            INSTANCE = newInstance
        }
    }

    private var authState: AuthState? = null

    /*
        When refreshing tokens, the Identity Server does not issue a new ID token
        The AppAuth code does not allow us to update the token response with the original ID token
        Therefore we store the ID token separately

        The above original comment (from demo project) is not very clear, but it was checked and
        saving this id separately seems to be really necessary for correct logout redirect
    */
    var idToken: String? = null

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

    /*
     * Manage storing or updating the token response
     */
    fun saveTokens(tokenResponse: TokenResponse) {

        if (tokenResponse.idToken != null) {
            this.idToken = tokenResponse.idToken
            Timber.d("serialize idToken: $idToken")
            authPrefs.edit()
                .putString("idToken", idToken)
                .apply()
        }

        this.authState!!.update(tokenResponse, null)

        val authStateJson = authState!!.jsonSerializeString()
        Timber.d("serialize authStateJson: $authStateJson")
        authPrefs.edit()
            .putString("stateJson", authStateJson)
            .apply()
    }

    /*
    * Clear tokens upon logout or when the session expires
    */
    fun clearTokens() {
        authState?.authorizationServiceConfiguration?.let {
            this.authState = AuthState(it)
        }
        this.idToken = null
        authPrefs.edit().clear().apply()
    }
}



