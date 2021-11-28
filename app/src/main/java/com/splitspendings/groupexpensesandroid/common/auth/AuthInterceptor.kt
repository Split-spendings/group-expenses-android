package com.splitspendings.groupexpensesandroid.common.auth

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class AuthInterceptor : Interceptor {

    private val authStateManager: AuthStateManager = AuthStateManager.getInstance()

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        // If token has been saved, add it to the request
        authStateManager.tokenResponse?.accessToken?.let {
            Timber.i("Auth Token: $it")
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }
}