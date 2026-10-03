package ru.university.taskcalendar

// Модель задачи. data class автоматически создаёт equals, toString, copy
data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val date: String,
    val isDone: Boolean = false
)