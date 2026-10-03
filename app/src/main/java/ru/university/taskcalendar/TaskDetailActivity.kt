package ru.university.taskcalendar

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TaskDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Достаём id, переданный через putExtra; -1 — значение по умолчанию, если id не пришёл
        val taskId = intent.getIntExtra(EXTRA_TASK_ID, -1)
        val task = SampleTasks.findById(taskId)
        if (task == null) {
            finish()
            return
        }

        findViewById<TextView>(R.id.tvDetailTitle).text = task.title
        findViewById<TextView>(R.id.tvDetailDate).text = getString(R.string.label_date, task.date)
        findViewById<TextView>(R.id.tvDetailStatus).setText(
            if (task.isDone) R.string.status_done else R.string.status_not_done
        )
        findViewById<TextView>(R.id.tvDetailDescription).text = task.description

        findViewById<Button>(R.id.btnBack).setOnClickListener { finish() }
    }

    companion object {
        // Ключ для передачи id через Intent — константа, чтобы не опечататься в двух местах
        const val EXTRA_TASK_ID = "extra_task_id"
    }
}