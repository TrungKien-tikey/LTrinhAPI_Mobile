package com.example.mobile_api.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Bằng cách này, nó sẽ tồn tại khi bạn điều hướng giữa 2 màn hình
    val taskListViewModel: TaskListViewModel = viewModel()

    NavHost(navController = navController, startDestination = "task_list") {

        // Màn hình 1: Danh sách công việc (Trang chủ)
        composable(route = "task_list") {
            TaskListScreen(
                viewModel = taskListViewModel,
                onTaskClick = { taskId ->
                    navController.navigate("task_detail/$taskId")
                }
            )
        }

        composable(
            route = "task_detail/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId") ?: 0
            val taskDetailViewModel: TaskDetailViewModel = viewModel()

            TaskDetailScreen(
                viewModel = taskDetailViewModel,
                taskId = taskId,
                onNavigateBack = { deletedTaskId ->
                    // 1. Kiểm tra xem có ID được gửi về không (tức là đã xóa)
                    if (deletedTaskId != null) {
                        // 2. Gọi ViewModel của List để xóa item khỏi state
                        taskListViewModel.removeTaskFromList(deletedTaskId)
                    }

                    // 3. Luôn luôn quay lại màn hình trước đó
                    navController.popBackStack()
                }
            )
        }
    }
}