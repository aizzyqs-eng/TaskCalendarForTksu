package ru.university.taskcalendar

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var taskDao: TaskDao
    private lateinit var adapter: TaskAdapter
    private lateinit var tvEmpty: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        taskDao = AppDatabase.getInstance(this).taskDao()
        tvEmpty = findViewById(R.id.tvEmpty)

        adapter = TaskAdapter { task ->
            val intent = Intent(this, TaskDetailActivity::class.java)
            intent.putExtra(TaskDetailActivity.EXTRA_TASK_ID, task.id)
            startActivity(intent)
        }
        val rvTasks = findViewById<RecyclerView>(R.id.rvTasks)
        rvTasks.layoutManager = LinearLayoutManager(this)
        rvTasks.adapter = adapter

        findViewById<FloatingActionButton>(R.id.fabAdd).setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
        }
    }

    // onResume вызывается каждый раз, когда экран снова становится видимым —
    // например, после возврата с экрана добавления или деталей. Поэтому список
    // перечитывается из базы и всегда актуален.
    override fun onResume() {
        super.onResume()
        lifecycleScope.launch { loadTasks() }
    }

    private suspend fun loadTasks() {
        // Запрос к базе — на потоке Dispatchers.IO, чтобы не тормозить интерфейс
        val tasks = withContext(Dispatchers.IO) { taskDao.getAll() }

        // Проверка, что база работает: вывод в Logcat (фильтр по тегу TaskDB)
        Log.d(TAG, "Загружено задач из базы: ${tasks.size}")
        tasks.forEach { Log.d(TAG, it.toString()) }

        adapter.setTasks(tasks)
        tvEmpty.isVisible = tasks.isEmpty()
    }

    companion object {
        private const val TAG = "TaskDB"
    }
}