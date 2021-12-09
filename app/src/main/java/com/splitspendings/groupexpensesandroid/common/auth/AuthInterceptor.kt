package com.splitspendings.groupexpensesandroid.common.auth

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.util.*

class AuthInterceptor : Interceptor {

    private val authStateManager: AuthStateManager = AuthStateManager.getInstance()
    private val appAuthHandler: AppAuthHandler = AppAuthHandler.getInstance()

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        var tokenResponse = authStateManager.tokenResponse

        //TODO perform this after getting 401 and then resend request
        // because token may become outdated during communication, can it?
        tokenResponse?.accessTokenExpirationTime?.let {
            val time = Date(it)
            val currentTime = Date()
            if (currentTime.before(time)) {
                Timber.i("currentTime $currentTime accessTokenExpirationTime: $time OK")
            } else {
                Timber.i("currentTime $currentTime accessTokenExpirationTime: $time EXPIRED")
                //tokenResponse = refreshToken()
            }
        }

        tokenResponse?.accessToken?.let {
            Timber.i("adding accessToken to header")
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        val request = requestBuilder.build()
        Timber.i("request: $request")
        //Timber.i("request headers: ${request.headers()}")

        val response = chain.proceed(request)
        Timber.i("response: $response")
        return response
    }

    /*private fun refreshToken(): TokenResponse {
        val refreshToken = authStateManager.tokenResponse?.refreshToken
        if (refreshToken.isNullOrBlank()) {
            Timber.e("refreshToken isNullOrBlank")
            throw AuthException("RefreshToken isNullOrBlank", null)
        }

        if (authStateManager.metadata == null) {
            Timber.d("fetchMetadata")
            authStateManager.metadata = appAuthHandler.fetchMetadata()
        }
        val metadata = authStateManager.metadata
        if (metadata == null) {
            Timber.e("metadata is null")
            throw AuthException("Metadata is null", null)
        }

        Timber.d("before calling refreshAccessToken")
        return appAuthHandler.refreshAccessToken(metadata, refreshToken)
    }*/
}