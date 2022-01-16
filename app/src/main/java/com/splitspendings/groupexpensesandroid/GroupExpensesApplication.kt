package com.splitspendings.groupexpensesandroid

import android.app.Application
import com.splitspendings.groupexpensesandroid.auth.AppAuthHandler
import com.splitspendings.groupexpensesandroid.auth.AuthStateManager
import com.splitspendings.groupexpensesandroid.repository.BalanceRepository
import com.splitspendings.groupexpensesandroid.repository.GroupRepository
import com.splitspendings.groupexpensesandroid.repository.PayoffRepository
import com.splitspendings.groupexpensesandroid.repository.SpendingRepository
import timber.log.Timber

class GroupExpensesApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        AppAuthHandler.init(applicationContext)

        val authPrefs = getSharedPreferences("auth", MODE_PRIVATE)
        AuthStateManager.init(authPrefs)

        GroupRepository.init(applicationContext)
        SpendingRepository.init(applicationContext)
        BalanceRepository.init(applicationContext)
        PayoffRepository.init(applicationContext)
    }
}