package com.example.sharedoc.api

import android.content.Context
import com.example.sharedoc.utils.SharedPrefsHelper
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val baseUrl = "http://10.22.15.92:8080/"

    private var retrofit: Retrofit? = null

    fun getApiService(context: Context): ApiService {
        if (retrofit == null) {
            val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val requestBuilder: Request.Builder = chain.request().newBuilder()

                    val token = SharedPrefsHelper.getToken(context)
                    if (!token.isNullOrEmpty()) {
                        requestBuilder.addHeader("Authorization", "Bearer $token")
                    }

                    chain.proceed(requestBuilder.build())
                }
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!.create(ApiService::class.java)
    }
}