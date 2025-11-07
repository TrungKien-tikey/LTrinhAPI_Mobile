package com.example.mobile_api.data

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface TaskApiService {
    @GET("https://amock.io/api/researchUTH/tasks")
    suspend fun getTasks(): TaskListResponse

    @GET("https://amock.io/api/researchUTH/task/{taskId}")
    suspend fun getTaskDetail(@Path("taskId") taskId: Int): TaskDetailResponse

    @DELETE("https://amock.io/api/researchUTH/task/{taskId}")
    suspend fun deleteTask(@Path("taskId") taskId: Int): Response<Unit>
}