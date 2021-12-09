package com.splitspendings.groupexpensesandroid.common.auth

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import java.util.*

const val UNAUTHORIZED = 401
const val AUTHORIZATION_HEADER = "Authorization"

class AuthInterceptor : Interceptor {

    private val authStateManager: AuthStateManager = AuthStateManager.getInstance()
    private val appAuthHandler: AppAuthHandler = AppAuthHandler.getInstance()

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        authStateManager.tokenResponse?.accessToken?.let {
            Timber.i("adding accessToken to Authorization header")
            requestBuilder.addHeader(AUTHORIZATION_HEADER, "Bearer $it")
        }

        var response = sendRequest(requestBuilder.build(), chain)

        if (response.code() == UNAUTHORIZED) {

            // TODO add coroutine
            val refreshedAccessToken = getRefreshedAccessToken()

            requestBuilder.addHeader(AUTHORIZATION_HEADER, "Bearer $refreshedAccessToken")
            response = sendRequest(requestBuilder.build(), chain)
        }

        return response
    }

    private fun sendRequest(request: Request, chain: Interceptor.Chain): Response {
        Timber.i("request: $request")
        //Timber.i("request headers: ${request.headers()}")
        val response = chain.proceed(request)
        Timber.i("response: $response")
        return response
    }

    private fun checkAccessTokenExpired(message: String) {
        authStateManager.tokenResponse?.accessTokenExpirationTime?.let {
            val time = Date(it)
            val currentTime = Date()
            Timber.i(
                "$message $currentTime accessTokenExpirationTime: $time " +
                        if (currentTime.before(time)) "OK" else "Expired"
            )
        }
    }

    private suspend fun getRefreshedAccessToken(): String {
        val refreshToken = authStateManager.tokenResponse?.refreshToken
        if (refreshToken.isNullOrBlank()) {
            Timber.e("refreshToken is null or blank")
            throw AuthException("RefreshToken is null or blank", null)
        }

        checkAccessTokenExpired("Before refresh: ")

        var metadata = authStateManager.metadata
        if (metadata == null) {
            Timber.d("metadata is null -> fetchMetadata")
            metadata = appAuthHandler.fetchMetadata()
            authStateManager.metadata = metadata
        }

        Timber.d("calling refreshAccessToken")
        val tokenResponse = appAuthHandler.refreshAccessToken(metadata, refreshToken)

        checkAccessTokenExpired("After refresh: ")

        return tokenResponse?.accessToken ?: throw AuthException("Could not refresh access token", null)
    }
}