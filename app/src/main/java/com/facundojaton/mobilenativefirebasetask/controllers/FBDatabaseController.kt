package com.facundojaton.mobilenativefirebasetask.controllers

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object FBDatabaseController {
    private const val LIST_ITEMS = "listItems"
    private const val USERS = "users"
    private const val DATA = "data"

    fun getUser(id: String): DatabaseReference =
        FirebaseDatabase.getInstance().getReference(USERS).child(id)

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