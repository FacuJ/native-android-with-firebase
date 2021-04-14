package com.facundojaton.mobilenativefirebasetask.controllers

import com.google.firebase.auth.FirebaseAuth


object SessionController {
    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    var authenticationState = if (FirebaseAuth.getInstance().currentUser != null) {
        AuthenticationState.AUTHENTICATED
    } else AuthenticationState.UNAUTHENTICATED

    var userEmail = FirebaseAuth.getInstance().currentUser?.email
    var userId = FirebaseAuth.getInstance().currentUser?.uid

    fun logout() {
        FirebaseAuth.getInstance().signOut()
        authenticationState = AuthenticationState.UNAUTHENTICATED
        userEmail = null
        userId = null
    }

    fun initializeSignInData() {
        authenticationState = AuthenticationState.AUTHENTICATED
        userEmail = FirebaseAuth.getInstance().currentUser?.email
        userId = FirebaseAuth.getInstance().currentUser?.uid
    }
}