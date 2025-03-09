package com.example.sharedoc.api

import com.example.sharedoc.model.ApiResponse
import com.example.sharedoc.model.Connection
import com.example.sharedoc.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("/auth/logIn")
    fun userLogIn(@Body user: User): Call<ApiResponse<String>>

    @POST("/auth/signUp")
    fun userSignUp(@Body user: User): Call<ApiResponse<User>>

    @GET("/connection/getconnects")
    fun getUserConnections(): Call<ApiResponse<Connection>>
}