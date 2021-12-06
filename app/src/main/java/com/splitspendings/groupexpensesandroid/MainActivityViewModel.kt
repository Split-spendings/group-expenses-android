package com.splitspendings.groupexpensesandroid

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.splitspendings.groupexpensesandroid.common.auth.AppAuthHandler

class MainActivityViewModel(val app: Application) : AndroidViewModel(app) {

    fun dispose() {
        AppAuthHandler.getInstance(app.applicationContext).dispose()
    }
}