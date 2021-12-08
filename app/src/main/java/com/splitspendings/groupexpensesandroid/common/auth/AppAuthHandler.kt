package com.splitspendings.groupexpensesandroid.common.auth

import android.content.Context
import android.content.Intent
import net.openid.appauth.AppAuthConfiguration
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.AuthorizationServiceConfiguration.fetchFromIssuer
import net.openid.appauth.EndSessionRequest
import net.openid.appauth.GrantTypeValues
import net.openid.appauth.ResponseTypeValues
import net.openid.appauth.TokenRequest
import net.openid.appauth.TokenResponse
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/*
 * Manage AppAuth integration in one class in order to reduce code in the rest of the app
 */
class AppAuthHandler(private val config: AuthConfig, context: Context) {

    companion object {
        @Volatile
        private var INSTANCE: AppAuthHandler? = null

        fun getInstance(): AppAuthHandler {
            synchronized(this) {
                return INSTANCE!!
            }
        }

        fun init(context: Context) {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = AppAuthHandler(AuthConfig.getInstance(context), context)
                    INSTANCE = instance
                }
            }
        }
    }

    private val authService = AuthorizationService(
        context,
        AppAuthConfiguration.Builder()
            .setConnectionBuilder(HttpConnectionBuilder.INSTANCE)
            .setSkipIssuerHttpsCheck(true)
            .build()
    )

    /*
     * Get OpenID Connect endpoints
     */
    suspend fun fetchMetadata(): AuthorizationServiceConfiguration {

        return suspendCoroutine { continuation ->

            fetchFromIssuer(this.config.getIssuerUri(), { metadata, ex ->

                when {
                    metadata != null -> {
                        Timber.i("Metadata retrieved successfully")
                        Timber.d(metadata.toJsonString())
                        continuation.resume(metadata)
                    }
                    else -> {
                        val error = createAuthorizationError("Metadata Download Error", ex)
                        continuation.resumeWithException(error)
                    }
                }
            }, HttpConnectionBuilder.INSTANCE)
        }
    }

    /*
     * Trigger a redirect with standard parameters
     * acr_values could be sent as an extra parameter, to control the authentication method
     */
    fun getAuthorizationRedirectIntent(metadata: AuthorizationServiceConfiguration): Intent {

        // Use acr_values to select a particular authentication method at runtime
        val extraParams = mutableMapOf<String, String>()
        //extraParams.put("acr_values", "urn:se:curity:authentication:html-form:Username-Password")

        val request = AuthorizationRequest.Builder(
            metadata,
            this.config.clientID,
            ResponseTypeValues.CODE,
            this.config.getRedirectUri()
        )
            .setScopes(this.config.scope)
            .setAdditionalParameters(extraParams)
            .build()

        return authService.getAuthorizationRequestIntent(request)
    }

    /*
     * Handle the authorization response, including the user closing the Chrome Custom Tab
     */
    fun handleAuthorizationResponse(
        response: AuthorizationResponse?,
        ex: AuthorizationException?
    ): AuthorizationResponse {

        if (response == null) {
            throw createAuthorizationError("Authorization Request Error", ex)
        }

        Timber.i("Authorization response received successfully")
        Timber.d("CODE: " + response.authorizationCode + ", STATE: " + response.state)
        return response
    }

    /*
     * Handle the authorization code grant request to get tokens
     */
    suspend fun redeemCodeForTokens(authResponse: AuthorizationResponse): TokenResponse? {

        return suspendCoroutine { continuation ->

            val extraParams = mutableMapOf<String, String>()
            val tokenRequest = authResponse.createTokenExchangeRequest(extraParams)

            authService.performTokenRequest(tokenRequest) { tokenResponse, ex ->

                when {
                    tokenResponse != null -> {
                        Timber.i("Authorization code grant response received successfully")
                        Timber.d("AT: " + tokenResponse.accessToken + ", RT: " + tokenResponse.refreshToken + ", IDT: " + tokenResponse.idToken)
                        continuation.resume(tokenResponse)
                    }
                    else -> {
                        val error = createAuthorizationError("Authorization Response Error", ex)
                        continuation.resumeWithException(error)
                    }
                }
            }
        }
    }

    /*
     * Try to refresh an access token and return null when the refresh token expires
     */
    suspend fun refreshAccessToken(
        metadata: AuthorizationServiceConfiguration,
        refreshToken: String
    ): TokenResponse? {

        return suspendCoroutine { continuation ->

            val extraParams = mutableMapOf<String, String>()
            val tokenRequest = TokenRequest.Builder(metadata, this.config.clientID)
                .setGrantType(GrantTypeValues.REFRESH_TOKEN)
                .setRefreshToken(refreshToken)
                .setAdditionalParameters(extraParams)
                .build()

            authService.performTokenRequest(tokenRequest) { tokenResponse, ex ->

                when {
                    tokenResponse != null -> {
                        Timber.i("Refresh token grant response received successfully")
                        Timber.d("AT: " + tokenResponse.accessToken + ", RT: " + tokenResponse.refreshToken + ", IDT: " + tokenResponse.idToken)
                        continuation.resume(tokenResponse)
                    }
                    else -> {

                        if (ex != null &&
                            ex.type == AuthorizationException.TYPE_OAUTH_TOKEN_ERROR &&
                            ex.code == AuthorizationException.TokenRequestErrors.INVALID_GRANT.code
                        ) {
                            Timber.i("Refresh token expired and the user must re-authenticate")
                            continuation.resume(null)

                        } else {

                            val error = createAuthorizationError("Token Refresh Error", ex)
                            continuation.resumeWithException(error)
                        }
                    }
                }
            }
        }
    }

    /*
     * Do an OpenID Connect end session redirect and remove the SSO cookie
     */
    fun getEndSessionRedirectIntent(
        metadata: AuthorizationServiceConfiguration,
        idToken: String?
    ): Intent {

        val extraParams = mutableMapOf<String, String>()
        val request = EndSessionRequest.Builder(metadata)
            .setIdTokenHint(idToken)
            .setPostLogoutRedirectUri(this.config.getPostLogoutRedirectUri())
            .setAdditionalParameters(extraParams)
            .build()

        return authService.getEndSessionRequestIntent(request)
    }

    /*
     * Finalize after receiving an end session response
     */
    fun handleEndSessionResponse(ex: AuthorizationException?) {

        when {
            ex != null -> {
                throw createAuthorizationError("End Session Request Error", ex)
            }
        }
    }

    /*
     * Clean up AppAuth resources on exit
     */
    fun dispose() {
        this.authService.dispose()
    }

    /*
     * Process standard OAuth error / error_description fields and also AppAuth error identifiers
     */
    private fun createAuthorizationError(title: String, ex: AuthorizationException?): AuthServerCommunicationException {

        val parts = mutableListOf<String>()

        if (ex?.type != null) {
            parts.add("(${ex.type} / ${ex.code})")
        }

        if (ex?.error != null) {
            parts.add(ex.error!!)
        }

        val description: String = if (ex?.errorDescription != null) {
            ex.errorDescription!!
        } else {
            UNKNOWN_ERROR
        }
        parts.add(description)

        val fullDescription = parts.joinToString(" : ")
        Timber.e(fullDescription)
        return AuthServerCommunicationException(title, fullDescription)
    }
}