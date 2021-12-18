package com.splitspendings.groupexpensesandroid

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import timber.log.Timber

class NavigationHandler(private val navController: NavController) {

    companion object {
        @Volatile
        private var INSTANCE: NavigationHandler? = null

        fun getInstance(): NavigationHandler {
            synchronized(this) {
                return INSTANCE!!
            }
        }

        fun init(navController: NavController) {
            synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = NavigationHandler(navController)
                }
            }
        }
    }

    fun navigateToLoggedOut() {
        Timber.d("navigate to logged out")
        val navOptions: NavOptions = NavOptions.Builder().setPopUpTo(R.id.groupsListFragment, true).build()
        navController.navigate(R.id.loginFragment, null, navOptions)
    }
}