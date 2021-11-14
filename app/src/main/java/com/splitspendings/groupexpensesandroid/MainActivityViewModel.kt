package com.splitspendings.groupexpensesandroid

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.splitspendings.groupexpensesandroid.common.auth.AppAuthHandler
import com.splitspendings.groupexpensesandroid.common.auth.AuthConfig
import com.splitspendings.groupexpensesandroid.common.auth.AuthConfigLoader
import com.splitspendings.groupexpensesandroid.common.auth.AuthStateManager

class MainActivityViewModel(app: Application) : AndroidViewModel(app) {

    // Global objects
    private val authConfig: AuthConfig = AuthConfigLoader().load(app.applicationContext)
    private val appAuthHandler: AppAuthHandler = AppAuthHandler(authConfig, app.applicationContext)
    private val authStateManager: AuthStateManager = AuthStateManager()

    fun dispose() {
        this.appAuthHandler.dispose()
    }
}