package com.splitspendings.groupexpensesandroid

import android.app.Application
import com.splitspendings.groupexpensesandroid.common.auth.AppAuthHandler
import timber.log.Timber

class GroupExpensesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        AppAuthHandler.init(applicationContext)
    }
}