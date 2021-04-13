package com.facundojaton.mobilenativefirebasetask.controllers

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object FBDatabaseController {
    private const val USERS = "users"

    fun getUser(id: String): DatabaseReference =
        FirebaseDatabase.getInstance().getReference(USERS).child(id)

}