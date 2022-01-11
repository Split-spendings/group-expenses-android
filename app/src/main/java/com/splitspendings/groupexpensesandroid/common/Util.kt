package com.splitspendings.groupexpensesandroid.common

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun closeKeyboard(fragment: Fragment) {
    fragment.activity?.let { activity ->
        activity.currentFocus?.let {
            val inputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}