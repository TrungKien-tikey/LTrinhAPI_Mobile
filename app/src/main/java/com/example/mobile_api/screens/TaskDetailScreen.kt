package com.example.mobile_api.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobile_api.data.Attachment
import com.example.mobile_api.data.Subtask
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
    // Dùng LazyColumn để toàn bộ nội dung có thể cuộn được
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // 1. Title
        item {
            Text(
                text = task.title,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        // 2. Description
        item {
            Text(
                text = task.description,
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // 3. Info Box (Category, Status, Priority)
        item {
            TaskInfoBox(task = task)
        }

        // 4. Subtasks
        // Chỉ hiển thị mục này nếu danh sách subtask không rỗng
        if (!task.subtasks.isNullOrEmpty()) {
            item {
                Text(
                    text = "Subtasks",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
                )
            }
            // Hiển thị danh sách các subtask
            items(task.subtasks) { subtask ->
                SubtaskItem(subtask = subtask)
            }
        }

        // 5. Attachments
        // Chỉ hiển thị mục này nếu danh sách attachment không rỗng
        if (!task.attachments.isNullOrEmpty()) {
            item {
                Text(
                    text = "Attachments",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
                )
            }
            // Hiển thị danh sách các attachment
            items(task.attachments) { attachment ->
                AttachmentItem(attachment = attachment)
            }
        }
    }
}

// Composable mới cho Info Box
@Composable
fun TaskInfoBox(task: TaskDetail) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        // Màu hồng nhạt như trong hình
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E5E5)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround // Chia đều không gian
        ) {
            InfoItem(
                icon = Icons.Default.Category,
                label = "Category",
                value = task.category ?: "N/A"
            )
            InfoItem(
                icon = Icons.Default.Task, // Icon cho Status
                label = "Status",
                value = task.status ?: "N/A"
            )
            InfoItem(
                icon = Icons.Default.Flag, // Icon cho Priority
                label = "Priority",
                value = task.priority ?: "N/A"
            )
        }
    }
}

// Composable mới cho từng mục trong Info Box
@Composable
fun InfoItem(icon: ImageVector, label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(imageVector = icon, contentDescription = label)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}

// Composable mới cho Subtask Item
@Composable
fun SubtaskItem(subtask: Subtask) {
    var isChecked by remember { mutableStateOf(subtask.isCompleted) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { isChecked = it }
            )
            Spacer(modifier = Modifier.width(8.dp))
            // Dữ liệu 'title' của subtask có thể là 'false'
            Text(text = subtask.title ?: "No title", fontSize = 16.sp)
        }
    }
}

// Composable mới cho Attachment Item
@Composable
fun AttachmentItem(attachment: Attachment) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.Link, contentDescription = "Attachment")
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = attachment.filename, fontSize = 16.sp)
        }
    }
}

