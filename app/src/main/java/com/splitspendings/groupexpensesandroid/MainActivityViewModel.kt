package com.splitspendings.groupexpensesandroid

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class MainActivityViewModel(val app: Application) : AndroidViewModel(app) {

    fun dispose() {
        //AppAuthHandler.getInstance(app.applicationContext).dispose()
    }
}