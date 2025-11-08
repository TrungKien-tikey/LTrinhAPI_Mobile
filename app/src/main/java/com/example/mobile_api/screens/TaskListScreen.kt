package com.example.mobile_api.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobile_api.data.Task
import com.example.mobile_api.R
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
        },

        // --- BẮT ĐẦU PHẦN THÊM MỚI ---
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO: Xử lý khi nhấn nút + (mở màn hình tạo task mới) */ }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        },
        // Đặt nút + ở giữa
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            BottomAppBar {
                // Thêm các icon cho bottom bar như trong hình mẫu
                Spacer(modifier = Modifier.weight(1f)) // Đẩy icon đầu tiên sang phải
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Default.Home, contentDescription = "Home")
                }
                Spacer(modifier = Modifier.weight(1f)) // Khoảng trống cho FAB
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Default.DateRange, contentDescription = "Calendar")
                }
                Spacer(modifier = Modifier.weight(1f)) // Đẩy icon cuối cùng sang phải
            }
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
                            item {
                                SmartTasksHeader() // Gọi composable header
                            }
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

    var isChecked by remember { mutableStateOf(task.status != "Pending") }

    val colorInProgress = Color(0xFFF5E4E3) // Màu tím nhạt cho "In Progress"
    val colorCompleted = Color(0xFFE5F3E5) // Màu xanh lá nhạt cho "Completed"

    // Màu mặc định (cho "Pending" và các trường hợp khác)
    val colorDefault = Color(0xFFA9B3EE)

    // 2. Quyết định màu dựa trên task.status
    val cardColor = when (task.status) {
        "In Progress" -> colorInProgress
        "Completed" -> colorCompleted
        "Pending" -> colorDefault
        else -> colorDefault
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick), // Xử lý click
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
        // Chúng ta sẽ xử lý màu nền (hồng, xanh lá, xanh dương) ở bước sau
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp) // Tinh chỉnh padding
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically // Căn giữa Checkbox và cột text
        ) {
            // 1. Checkbox
            Checkbox(
                checked = isChecked,
                onCheckedChange = { isChecked = it }
            )

            Spacer(modifier = Modifier.width(12.dp))

            // 2. Cột chứa nội dung text
            Column(modifier = Modifier.weight(1f)) {

                // Title
                Text(text = task.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)

                Spacer(modifier = Modifier.height(8.dp))

                // 3. Hàng dưới cùng chứa Status và DueDate
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    // Dùng SpaceBetween để đẩy 2 text về 2 phía
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Status
                    Text(text = "Status: ${task.status}", fontSize = 14.sp)

                    // DueDate
                    if (task.dueDate != null) {
                        Text(
                            text = task.dueDate,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun SmartTasksHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp), // Padding cho header
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1. Logo UTH
        Image(
            // **LƯU Ý**: Bạn cần thêm file logo (ví dụ: uth_logo.png)
            // vào thư mục res/drawable của bạn
            painter = painterResource(id = R.drawable.uthh),
            contentDescription = "UTH Logo",
            modifier = Modifier.size(50.dp), // Kích thước logo
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.width(12.dp))

        // 2. Tên ứng dụng và mô tả
        Column {
            Text(
                text = "SmartTasks",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "A simple and efficient to-do app",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.weight(1f)) // Đẩy chuông sang phải

        // 3. Icon chuông (bell icon)
        Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = "Notifications",
            modifier = Modifier.size(28.dp),
            tint = Color(0xFFE8B923) // Màu vàng cho chuông
        )
    }
}