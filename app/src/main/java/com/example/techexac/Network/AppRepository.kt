package com.example.techexac.Network

class AppRepository {

    private val apiService: CommonApiService? by lazy { RetrofitInstance.commonApiService }

    suspend fun postRequest() = apiService?.callRetrofit()

}