package ru.university.taskcalendar

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private val onTaskClick: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    // Теперь список не фиксированный: он приходит из базы и может обновляться
    private var tasks: List<Task> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setTasks(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged() // перерисовать список
    }

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        val context = holder.itemView.context

        holder.tvTitle.text = task.title
        holder.tvDate.text = DateUtils.toUi(task.date)

        if (task.isDone) {
            holder.tvStatus.setText(R.string.status_done)
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.status_done))
        } else {
            holder.tvStatus.setText(R.string.status_not_done)
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.status_not_done))
        }

        holder.itemView.setOnClickListener { onTaskClick(task) }
    }

    override fun getItemCount(): Int = tasks.size
}