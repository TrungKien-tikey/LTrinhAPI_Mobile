package com.example.mobile_api.data
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.JsonQualifier

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class HandleBooleanAsTitle
@JsonClass(generateAdapter = true)
data class TaskListResponse(
    // Tên "tasks" phải khớp với key trong JSON
    @Json(name = "data") val tasks: List<Task>
)
@JsonClass(generateAdapter = true)
data class TaskDetailResponse(
    @Json(name = "data") val data: TaskDetail?
)
@JsonClass(generateAdapter = true)
data class Task(
    @Json(name = "id") val id: Int,
    @Json(name = "title") val title: String,
    @Json(name = "status") val status: String,
    @Json(name = "dueDate") val dueDate: String?
)

@JsonClass(generateAdapter = true)
data class TaskDetail(
    @Json(name = "id") val id: Int,
    @Json(name = "title") val title: String,
    @Json(name = "description") val description: String,
    @Json(name = "category") val category: String?,
    @Json(name = "status") val status: String?,
    @Json(name = "priority") val priority: String?,
    @Json(name = "subtasks") val subtasks: List<Subtask>?,
    @Json(name = "attachments") val attachments: List<Attachment>?
)

@JsonClass(generateAdapter = true)
data class Subtask(
    @Json(name = "id") val id: Int,
    @HandleBooleanAsTitle
    @Json(name = "title") val title: String?,

    @Json(name = "isCompleted") val isCompleted: Boolean
)

@JsonClass(generateAdapter = true)
data class Attachment(
    @Json(name = "fileName") val filename: String
)