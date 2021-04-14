package com.facundojaton.mobilenativefirebasetask.controllers

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * Controller with methods that use the Firebase Realtime Database API
 */
object FBDatabaseController {
    private const val USERS = "users"
    private const val DATA = "data"

    fun getListItemsReference(userId: String): DatabaseReference =
        FirebaseDatabase.getInstance().getReference(USERS).child("$userId/$DATA")

    fun pushItem(userId: String): DatabaseReference =
        FirebaseDatabase.getInstance().getReference(USERS)
            .child("$userId/$DATA")
            .push()

    fun getItemReference(userId: String, itemId: String): DatabaseReference =
        FirebaseDatabase.getInstance().getReference(USERS)
            .child("$userId/$DATA").child(itemId)
}