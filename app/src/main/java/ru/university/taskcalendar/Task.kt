package ru.university.taskcalendar

import androidx.room.Entity
import androidx.room.PrimaryKey

// @Entity — этот класс описывает таблицу "tasks" в базе данных.
// Каждое поле класса становится столбцом таблицы.
@Entity(tableName = "tasks")
data class Task(
    // Первичный ключ; autoGenerate — база сама выдаёт новый уникальный id
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val date: String,           // хранится в формате "yyyy-MM-dd", например "2026-10-06"
    val time: String? = null,   // "HH:mm"; понадобится для напоминаний (этап 7)
    val isDone: Boolean = false
)