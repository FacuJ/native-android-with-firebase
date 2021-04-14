package com.facundojaton.mobilenativefirebasetask.utils

import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment


fun Fragment.hideKeyboard() {
    val view = activity?.currentFocus
    context?.let { fragmentContext ->
        view?.let {
            val imm =
                ContextCompat.getSystemService(fragmentContext, InputMethodManager::class.java)
            imm?.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}