package com.example.mobile_api.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_api.data.TaskDetail
import com.example.mobile_api.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskDetailViewModel : ViewModel() {

    private val repository = TaskRepository()

    // State cho chi tiết task
    private val _taskDetail = MutableStateFlow<Resource<TaskDetail?>>(Resource.Loading())
    val taskDetail: StateFlow<Resource<TaskDetail?>> = _taskDetail.asStateFlow()

    // State cho hành động xóa
    private val _deleteStatus =
        MutableStateFlow<Resource<Unit>>(Resource.Empty()) // Dùng Empty làm trạng thái chờ
    val deleteStatus: StateFlow<Resource<Unit>> = _deleteStatus.asStateFlow()

    fun fetchTaskDetail(taskId: Int) {
        _taskDetail.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val detail = repository.getTaskDetail(taskId)
                if (detail != null) {
                    // Nếu có dữ liệu, hiển thị Success
                    _taskDetail.value = Resource.Success(detail)
                } else {
                    // Nếu API trả về "data": null, báo lỗi
                    _taskDetail.value = Resource.Error("Task data is null or corrupted")
                }
            } catch (e: Exception) {
                // (Phần này xử lý lỗi HTTP 404)
                _taskDetail.value = Resource.Error(e.message ?: "Error fetching detail")
            }
        }
    }

    fun deleteTask(taskId: Int) {
        _deleteStatus.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val success = repository.deleteTask(taskId)
                if (success) {
                    _deleteStatus.value = Resource.Success(Unit) // Xóa thành công
                } else {
                    _deleteStatus.value = Resource.Error("Failed to delete task")
                }
            } catch (e: Exception) {
                _deleteStatus.value = Resource.Error(e.message ?: "Error deleting task")
            }
        }
    }
}