package ru.university.taskcalendar

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Простая форма добавления. Выбор даты через календарь-диалог появится на этапе 5.
class AddTaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_task)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etDescription = findViewById<EditText>(R.id.etDescription)
        val etDate = findViewById<EditText>(R.id.etDate)
        val btnSave = findViewById<Button>(R.id.btnSave)

        etDate.setText(DateUtils.todayUi()) // по умолчанию — сегодня

        btnSave.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val dbDate = DateUtils.toDb(etDate.text.toString())

            // Проверка ввода
            if (title.isEmpty()) {
                etTitle.error = getString(R.string.error_empty_title)
                return@setOnClickListener
            }
            if (dbDate == null) {
                etDate.error = getString(R.string.error_bad_date)
                return@setOnClickListener
            }

            val task = Task(
                title = title,
                description = etDescription.text.toString().trim(),
                date = dbDate
            )

            btnSave.isEnabled = false
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    AppDatabase.getInstance(this@AddTaskActivity).taskDao().insert(task)
                }
                Toast.makeText(this@AddTaskActivity, R.string.task_saved, Toast.LENGTH_SHORT).show()
                finish() // возвращаемся к списку, он обновится в onResume
            }
        }

        findViewById<Button>(R.id.btnBack).setOnClickListener { finish() }
    }
}