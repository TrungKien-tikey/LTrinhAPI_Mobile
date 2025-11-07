package com.example.mobile_api.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_api.data.Task
import com.example.mobile_api.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskListViewModel : ViewModel() {

    private val repository = TaskRepository()

    // Dùng StateFlow cho Compose
    private val _tasks = MutableStateFlow<Resource<List<Task>>>(Resource.Loading())
    val tasks: StateFlow<Resource<List<Task>>> = _tasks.asStateFlow()

    init {
        fetchTasks()
    }

    fun fetchTasks() {
        _tasks.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val taskList = repository.getTasks()
                if (taskList.isEmpty()) {
                    _tasks.value = Resource.Empty()
                } else {
                    _tasks.value = Resource.Success(taskList)
                }
            } catch (e: Exception) {
                _tasks.value = Resource.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
    fun removeTaskFromList(taskId: Int) {
        // Chỉ thực hiện nếu state hiện tại là Success
        val currentState = _tasks.value
        if (currentState is Resource.Success) {
            // Lấy danh sách hiện tại
            val currentList = currentState.data

            // Tạo một danh sách MỚI bằng cách lọc bỏ task có ID trùng
            if (currentList != null) {

                // Dòng code này bây giờ đã an toàn
                val newList = currentList.filterNot { it.id == taskId }

                // Cập nhật StateFlow với danh sách mới
                if (newList.isEmpty()) {
                    _tasks.value = Resource.Empty()
                } else {
                    _tasks.value = Resource.Success(newList)
                }
            }
        }
    }
}