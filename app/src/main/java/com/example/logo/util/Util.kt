package com.example.logo.util

import android.app.Activity
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyboard(){
    val view = currentFocus
    view?.let {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}