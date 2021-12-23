package com.splitspendings.groupexpensesandroid.screens.logout

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.splitspendings.groupexpensesandroid.auth.AppAuthHandler
import com.splitspendings.groupexpensesandroid.auth.AuthException
import com.splitspendings.groupexpensesandroid.auth.AuthStateManager
import kotlinx.coroutines.launch
import net.openid.appauth.AuthorizationException
import timber.log.Timber

class LogoutViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(LogoutViewModel::class.java)) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return LogoutViewModel(application) as T
    }
}


class LogoutViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val authStateManager: AuthStateManager = AuthStateManager.getInstance()
    private val appAuthHandler: AppAuthHandler = AppAuthHandler.getInstance()

    private val _eventNavigateToLoggedOut = MutableLiveData<Boolean>()
    val eventNavigateToLoggedOut: LiveData<Boolean>
        get() = _eventNavigateToLoggedOut

    private val _eventLogoutRedirectStart = MutableLiveData<Intent>()
    val eventLogoutRedirectStart: LiveData<Intent>
        get() = _eventLogoutRedirectStart

    init {
        _eventNavigateToLoggedOut.value = false
        _eventLogoutRedirectStart.value = null
    }

    fun onEventNavigateToLoggedOutComplete() {
        _eventNavigateToLoggedOut.value = false
    }

    fun onEventLogoutRedirectStartComplete() {
        _eventLogoutRedirectStart.value = null
    }

    /*
     * Build the logout redirect URL and then ask the view to redirect
     */
    fun startLogout() {

        viewModelScope.launch {
            try {
                if (authStateManager.metadata == null) {
                    Timber.d("fetchMetadata")
                    authStateManager.metadata = appAuthHandler.fetchMetadata()
                }
                if (authStateManager.metadata != null) {
                    Timber.d("before staring logout redirect intent")
                    val metadata = authStateManager.metadata!!
                    val idToken = authStateManager.idToken
                    val intent = appAuthHandler.getEndSessionRedirectIntent(metadata, idToken)
                    _eventLogoutRedirectStart.value = intent
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
    fun endLogout(data: Intent?) {

        if (data == null) {
            Timber.e("intent is null")
            return
        }

        viewModelScope.launch {
            try {
                appAuthHandler.handleEndSessionResponse(AuthorizationException.fromIntent(data))
                authStateManager.clearTokens()
                _eventNavigateToLoggedOut.value = true

            } catch (ex: AuthException) {
                Timber.e(ex)
            }
        }
    }
}