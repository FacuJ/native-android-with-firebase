package com.facundojaton.mobilenativefirebasetask.data.model

import java.io.Serializable

data class DatabaseItem(
    var id: String = "",
    var text: String = "",
) : Serializable
