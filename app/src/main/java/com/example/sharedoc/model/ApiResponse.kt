package com.example.sharedoc.model

data class ApiResponse<T>(
    val status: String,
    val message: String,
    val data: T? = null
)
