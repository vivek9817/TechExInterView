package com.example.techexac.Network

import com.example.techexac.Network.ApiEndPoins.ApiEndPoints
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitInstance {
    companion object {
        private var CONNECTION_TIMEOUT = 180L
        private var READ_TIMEOUT = 180L
        private var WRITE_TIMEOUT = 180L
        private var CALL_TIMEOUT = 180L

        private val retrofitLogin by lazy {
            val login = HttpLoggingInterceptor()
            login.setLevel(HttpLoggingInterceptor.Level.BODY)

            val gsonNullable = GsonBuilder()
                .setLenient()
                .serializeNulls()
                .create()

            val client = OkHttpClient.Builder()
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .callTimeout(CALL_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(login)
                .build()

            Retrofit.Builder()
                .baseUrl(" https://navkiraninfotech.com/g-mee-api/api/v1/")
                .addConverterFactory(GsonConverterFactory.create(gsonNullable))
                .client(client)
                .build()
        }
        val commonApiService by lazy {
            retrofitLogin.create(CommonApiService::class.java)
        }
    }
}