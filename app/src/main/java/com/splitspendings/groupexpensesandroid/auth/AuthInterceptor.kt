package com.splitspendings.groupexpensesandroid.auth

import com.splitspendings.groupexpensesandroid.NavigationHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
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
            //Timber.d("adding accessToken to Authorization header")
            requestBuilder.addHeader(AUTHORIZATION_HEADER, "Bearer $it")
        }

        var response = sendRequest(requestBuilder.build(), chain)

        if (response.code() == UNAUTHORIZED) {

            refreshAccessToken()

            authStateManager.tokenResponse?.accessToken?.let {
                //Timber.d("adding accessToken to Authorization header after refresh")
                requestBuilder.removeHeader(AUTHORIZATION_HEADER)
                requestBuilder.addHeader(AUTHORIZATION_HEADER, "Bearer $it")
                response = sendRequest(requestBuilder.build(), chain)
            }
        }

        if (response.code() == UNAUTHORIZED) {
            //Timber.d("navigate to logged out")
            authStateManager.clearTokens()
            navigateToLoggedOut()
        }

        return response
    }

    private fun navigateToLoggedOut() = runBlocking {
        withContext(Dispatchers.Main) {
            NavigationHandler.getInstance().navigateToLoggedOut()
        }
    }

    private fun sendRequest(request: Request, chain: Interceptor.Chain): Response {
        Timber.d("request: $request")
        //Timber.d("headers: ${request.headers()}")
        val response = chain.proceed(request)
        Timber.d("response: $response")
        return response
    }

    private fun refreshAccessToken() = runBlocking {
        try {
            val refreshToken = authStateManager.tokenResponse?.refreshToken
            if (refreshToken.isNullOrBlank()) {
                Timber.e("refreshToken is null or blank")

            } else {

                //checkAccessTokenExpired("before refresh")

                var metadata = authStateManager.metadata
                if (metadata == null) {
                    //Timber.d("metadata is null -> calling fetchMetadata")
                    metadata = appAuthHandler.fetchMetadata()
                    authStateManager.metadata = metadata
                }

                //Timber.d("calling refreshAccessToken")
                val tokenResponse = appAuthHandler.refreshAccessToken(metadata, refreshToken)

                if (tokenResponse == null) {
                    Timber.e("tokenResponse is null")
                } else {
                    authStateManager.saveTokens(tokenResponse)
                    //checkAccessTokenExpired("after refresh")
                }
            }
        } catch (ex: Exception) {
            Timber.e(ex)
        }
    }

    private fun checkAccessTokenExpired(message: String) {
        authStateManager.tokenResponse?.accessTokenExpirationTime?.let {
            val time = Date(it)
            val currentTime = Date()
            Timber.d(
                "$message: accessTokenExpirationTime: $time " +
                        if (currentTime.before(time)) "OK" else "Expired"
            )
        }
    }
}