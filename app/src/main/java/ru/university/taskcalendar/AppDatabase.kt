package ru.university.taskcalendar

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import java.time.LocalDate

// База данных приложения: объединяет все таблицы (entities) и DAO.
@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {
        // Единственный экземпляр базы на всё приложение (синглтон).
        // @Volatile — изменения сразу видны всем потокам.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "task_calendar.db"
                )
                    .addCallback(DemoDataCallback())
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }

    // Вызывается ОДИН раз — когда файл базы создаётся впервые (первый запуск приложения).
    // Заполняем базу несколькими задачами для демонстрации.
    private class DemoDataCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val today = LocalDate.now()
            insertDemo(db, "Подготовить отчёт по практике", "Описать все этапы и приложить скриншоты", today.plusDays(1), false)
            insertDemo(db, "Разобраться с RecyclerView", "Adapter, ViewHolder, LayoutManager", today, true)
            insertDemo(db, "Купить продукты", "Молоко, хлеб, яйца, овощи", today.plusDays(2), false)
            insertDemo(db, "Записаться в спортзал", "Узнать расписание и цены", today.plusDays(4), false)
        }

        private fun insertDemo(db: SupportSQLiteDatabase, title: String, description: String, date: LocalDate, isDone: Boolean) {
            db.execSQL(
                "INSERT INTO tasks (title, description, date, time, isDone) VALUES (?, ?, ?, NULL, ?)",
                arrayOf<Any?>(title, description, date.toString(), if (isDone) 1 else 0)
            )
        }
    }
}