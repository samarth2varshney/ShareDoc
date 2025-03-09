package com.example.sharedoc.model

import java.util.Date

data class Connection(
    val connectionid: String = "",
    val senderid: String = "",
    val receiverid: String = "",
    var status: String = "",
    var requesttime: Date = Date()
)
