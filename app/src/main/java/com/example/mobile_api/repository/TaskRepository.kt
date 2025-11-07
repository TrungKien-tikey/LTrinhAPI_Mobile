package com.example.mobile_api.repository

import com.example.mobile_api.data.RetrofitInstance
import com.example.mobile_api.data.Task
import com.example.mobile_api.data.TaskDetail
class TaskRepository {

    private val apiService = RetrofitInstance.api

    suspend fun getTasks(): List<Task> {
        val response = apiService.getTasks() // response bây giờ là TaskListResponse
        return response.tasks // Trả về danh sách tasks bên trong
    }

    suspend fun getTaskDetail(taskId: Int): TaskDetail? {
        val response = apiService.getTaskDetail(taskId) // Giờ trả về TaskDetailResponse
        return response.data // Trả về đối tượng TaskDetail bên trong
    }

    suspend fun deleteTask(taskId: Int): Boolean {
        // Trả về true nếu response.isSuccessful (code 200-299)
        return apiService.deleteTask(taskId).isSuccessful
    }
}