package ru.university.taskcalendar

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskDetailActivity : AppCompatActivity() {

    private lateinit var taskDao: TaskDao
    private var currentTask: Task? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        taskDao = AppDatabase.getInstance(this).taskDao()
        val taskId = intent.getIntExtra(EXTRA_TASK_ID, -1)

        // Загружаем задачу из базы по id
        lifecycleScope.launch {
            val task = withContext(Dispatchers.IO) { taskDao.getById(taskId) }
            if (task == null) {
                finish()
                return@launch
            }
            showTask(task)
        }

        // UPDATE: переключаем статус «выполнена / не выполнена»
        findViewById<Button>(R.id.btnToggleDone).setOnClickListener {
            val task = currentTask ?: return@setOnClickListener
            val updated = task.copy(isDone = !task.isDone)
            lifecycleScope.launch {
                withContext(Dispatchers.IO) { taskDao.update(updated) }
                showTask(updated)
            }
        }

        // DELETE: удаление с подтверждением
        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            val task = currentTask ?: return@setOnClickListener
            AlertDialog.Builder(this)
                .setTitle(R.string.delete_title)
                .setMessage(getString(R.string.delete_message, task.title))
                .setPositiveButton(R.string.delete) { _, _ ->
                    lifecycleScope.launch {
                        withContext(Dispatchers.IO) { taskDao.delete(task) }
                        Toast.makeText(this@TaskDetailActivity, R.string.task_deleted, Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                .setNegativeButton(R.string.cancel, null)
                .show()
        }

        findViewById<Button>(R.id.btnBack).setOnClickListener { finish() }
    }

    private fun showTask(task: Task) {
        currentTask = task
        findViewById<TextView>(R.id.tvDetailTitle).text = task.title
        findViewById<TextView>(R.id.tvDetailDate).text =
            getString(R.string.label_date, DateUtils.toUi(task.date))
        findViewById<TextView>(R.id.tvDetailStatus).setText(
            if (task.isDone) R.string.status_done else R.string.status_not_done
        )
        findViewById<TextView>(R.id.tvDetailDescription).text = task.description
        findViewById<Button>(R.id.btnToggleDone).setText(
            if (task.isDone) R.string.mark_not_done else R.string.mark_done
        )
    }

    companion object {
        const val EXTRA_TASK_ID = "extra_task_id"
    }
}