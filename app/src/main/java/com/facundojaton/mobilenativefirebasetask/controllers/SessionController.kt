package com.facundojaton.mobilenativefirebasetask.controllers

import androidx.lifecycle.map
import com.facundojaton.mobilenativefirebasetask.data.FirebaseUserLiveData


object SessionController {
    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    val authenticationState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }
}