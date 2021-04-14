package com.facundojaton.mobilenativefirebasetask.controllers

import androidx.lifecycle.map
import com.facundojaton.mobilenativefirebasetask.data.FirebaseUserLiveData
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


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
    val userId: String? = FirebaseAuth.getInstance().currentUser?.uid
    var userEmail: String? = null
}