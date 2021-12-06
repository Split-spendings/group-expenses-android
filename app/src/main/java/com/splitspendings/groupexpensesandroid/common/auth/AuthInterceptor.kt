package com.splitspendings.groupexpensesandroid.common.auth

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.util.*

class AuthInterceptor : Interceptor {

    private val authStateManager: AuthStateManager = AuthStateManager.getInstance()

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        authStateManager.tokenResponse?.accessTokenExpirationTime?.let {
            val time = Date(it)
            val currentTime = Date()
            if(currentTime.before(time)) {
                Timber.i("currentTime $currentTime accessTokenExpirationTime: $time OK")
            } else {
                Timber.i("currentTime $currentTime accessTokenExpirationTime: $time EXPIRED")
                // TODO refresh token
            }
        }

        // If token has been saved, add it to the request
        authStateManager.tokenResponse?.accessToken?.let {
            Timber.i("Auth Token: $it")
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        //return chain.proceed(requestBuilder.build())

        val request = requestBuilder.build()
        Timber.i("request: $request")
        Timber.i("request headers: ${request.headers()}")
        val response = chain.proceed(request)
        Timber.i("response: $response")
        return response
    }
}