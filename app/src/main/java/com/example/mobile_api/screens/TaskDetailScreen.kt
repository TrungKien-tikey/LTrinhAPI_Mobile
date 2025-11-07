package com.example.mobile_api.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobile_api.data.TaskDetail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    viewModel: TaskDetailViewModel,
    taskId: Int,
    onNavigateBack: (deletedTaskId: Int?) -> Unit
) {
    val context = LocalContext.current
    val detailState by viewModel.taskDetail.collectAsStateWithLifecycle()
    val deleteState by viewModel.deleteStatus.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = taskId) {
        viewModel.fetchTaskDetail(taskId)
    }

    LaunchedEffect(key1 = deleteState) {
        if (deleteState is Resource.Success) {
            Toast.makeText(context, "Task deleted successfully", Toast.LENGTH_SHORT).show()
            onNavigateBack(taskId)
        }
        if (deleteState is Resource.Error) {
            Toast.makeText(context, (deleteState as Resource.Error).message, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail") },
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack(null) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.deleteTask(taskId) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (detailState) {
                is Resource.Loading -> {
                    CircularProgressIndicator()
                }
                is Resource.Success -> {
                    val task = detailState.data
                    if (task != null) {
                        TaskDetailContent(task = task)
                    }
                }
                is Resource.Error -> {
                    Text(
                        text = detailState.message ?: "Error",
                        color = Color.Red
                    )
                }
                is Resource.Empty -> {  }
            }
        }
    }
}

// Composable cho nội dung chi tiết
@Composable
fun TaskDetailContent(task: TaskDetail) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = task.title, fontWeight = FontWeight.Bold, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = task.description, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Status: ${task.status ?: "N/A"}")
        Text(text = "Priority: ${task.priority ?: "N/A"}")
        Text(text = "Category: ${task.category ?: "N/A"}")
        // Bạn có thể dùng LazyColumn ở đây để hiển thị Subtasks và Attachments
    }
}

