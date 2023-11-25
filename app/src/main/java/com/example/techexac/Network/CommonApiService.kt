package com.example.techexac.Network

import com.example.techexac.Model.ApplicationsResponse
import retrofit2.Response
import retrofit2.http.*

interface CommonApiService {
    @POST("apps/list?kid_id=378")
    suspend fun callRetrofit() : Response<ApplicationsResponse>
}