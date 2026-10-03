package ru.university.taskcalendar

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

// DAO (Data Access Object) — список операций, которые можно выполнять с таблицей.
// Room сам генерирует код этих методов по аннотациям.
// suspend — метод выполняется асинхронно (в корутине), не блокируя интерфейс.
@Dao
interface TaskDao {

    @Insert
    suspend fun insert(task: Task): Long

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT * FROM tasks ORDER BY date ASC")
    suspend fun getAll(): List<Task>

    @Query("SELECT * FROM tasks WHERE date = :date ORDER BY time ASC")
    suspend fun getByDate(date: String): List<Task>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getById(id: Int): Task?
}