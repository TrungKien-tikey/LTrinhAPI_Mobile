package com.example.mobile_api.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobile_api.data.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    viewModel: TaskListViewModel,
    onTaskClick: (Int) -> Unit // Hàm callback để điều hướng
) {
    // Lắng nghe StateFlow từ ViewModel
    val tasksState by viewModel.tasks.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("UTH SmartTasks") })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            // Dùng when để xử lý các trạng thái
            when (tasksState) {
                is Resource.Loading -> {
                    CircularProgressIndicator()
                }
                is Resource.Empty -> {
                    // Yêu cầu: Hiển thị EmptyView
                    Text(
                        text = "No Tasks Yet! Stay productive...",
                        fontSize = 18.sp,
                        color = Color.Gray
                    )
                }
                is Resource.Success -> {
                    val tasks = (tasksState as Resource.Success<List<Task>>).data
                    if (tasks != null) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            items(tasks) { task ->
                                TaskItem(task = task, onClick = { onTaskClick(task.id) })
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    Text(
                        text = (tasksState as Resource.Error).message ?: "Error",
                        color = Color.Red
                    )
                }
            }
        }
    }
}

@Composable
fun TaskItem(task: Task, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick), // Xử lý click
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = task.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Status: ${task.status}", fontSize = 14.sp)
            if (task.dueDate != null) {
                Text(text = "Due: ${task.dueDate}", fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}

