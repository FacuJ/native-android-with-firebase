package com.facundojaton.mobilenativefirebasetask.data.model

import java.io.Serializable

/**
 *  Data class that stores de values from items retrieved or to be stored in Firebase
 */
data class DatabaseItem(
    var id: String = "",
    var text: String = "",
) : Serializable
