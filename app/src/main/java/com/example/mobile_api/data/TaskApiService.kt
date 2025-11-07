package com.example.mobile_api.data

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface TaskApiService {
    @GET("researchUTH/tasks")
    suspend fun getTasks(): TaskListResponse

    @GET("researchUTH/task/{taskId}")
    suspend fun getTaskDetail(@Path("taskId") taskId: Int): TaskDetailResponse // <-- SỬA Ở ĐÂY

    @DELETE("researchUTH/task/{taskId}")
    suspend fun deleteTask(@Path("taskId") taskId: Int): Response<Unit>
}