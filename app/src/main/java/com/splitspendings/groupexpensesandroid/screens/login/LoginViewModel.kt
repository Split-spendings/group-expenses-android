package com.splitspendings.groupexpensesandroid.screens.login

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.splitspendings.groupexpensesandroid.common.auth.AppAuthHandler
import com.splitspendings.groupexpensesandroid.common.auth.AuthException
import com.splitspendings.groupexpensesandroid.common.auth.AuthStateManager
import kotlinx.coroutines.launch
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import timber.log.Timber

class LoginViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return LoginViewModel(application) as T
    }
}


class LoginViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val authStateManager: AuthStateManager = AuthStateManager.getInstance()
    private val appAuthHandler: AppAuthHandler = AppAuthHandler.getInstance()

    private val _eventNavigateToLoggedIn = MutableLiveData<Boolean>()
    val eventNavigateToLoggedIn: LiveData<Boolean>
        get() = _eventNavigateToLoggedIn

    private val _eventLoginRedirectStart = MutableLiveData<Intent>()
    val eventLoginRedirectStart: LiveData<Intent>
        get() = _eventLoginRedirectStart

    init {
        _eventNavigateToLoggedIn.value = false
        _eventLoginRedirectStart.value = null
    }

    fun onEventNavigateToLoggedInComplete() {
        _eventNavigateToLoggedIn.value = false
    }

    fun onEventLoginRedirectStartComplete() {
        _eventLoginRedirectStart.value = null
    }

    /*
     * Build the authorization redirect URL and then ask the view to redirect
     */
    fun startLogin() {

        viewModelScope.launch {
            try {
                if (authStateManager.metadata == null) {
                    Timber.d("fetchMetadata")
                    authStateManager.metadata = appAuthHandler.fetchMetadata()
                }
                if (authStateManager.metadata != null) {
                    Timber.d("before staring login redirect intent")
                    val metadata = authStateManager.metadata!!
                    val intent = appAuthHandler.getAuthorizationRedirectIntent(metadata)
                    _eventLoginRedirectStart.value = intent
                } else {
                    Timber.e("metadata is null")
                }

            } catch (ex: AuthException) {
                Timber.e(ex)
            }
        }
    }

    /*
     * Redeem the code for tokens and also handle failures or the user cancelling the Chrome Custom Tab
     */
    fun endLogin(data: Intent?) {
        if (data == null) {
            Timber.e("intent is null")
            return
        }

        viewModelScope.launch {
            try {

                val authorizationResponse = appAuthHandler.handleAuthorizationResponse(
                    AuthorizationResponse.fromIntent(data),
                    AuthorizationException.fromIntent(data)
                )

                val tokenResponse = appAuthHandler.redeemCodeForTokens(authorizationResponse)

                if (tokenResponse == null) {
                    Timber.e("tokenResponse is null")
                } else {
                    Timber.d("accessToken: ${tokenResponse.accessToken}")
                    authStateManager.saveTokens(tokenResponse)
                    _eventNavigateToLoggedIn.value = true
                }

            } catch (ex: AuthException) {
                Timber.e(ex)
            }
        }
    }
}