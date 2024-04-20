package com.mods.pink.house.interviewtask.api

import com.mods.pink.house.interviewtask.model.ApiResponseItem
import retrofit2.http.GET

interface ApiService {

    @GET("content/misc/media-coverages?limit=100")
    suspend fun getData(): List<ApiResponseItem>
}