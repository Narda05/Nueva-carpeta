package com.example.final1

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView // Asumiendo que tienes TextViews en todo_row.xml [1]
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

//Its main responsibility is to take your data and display each item of that data in the RecyclerView list.

class TasksAdapter(val context: Context, private val tasks: MutableList<Task>
) : RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

    // ViewHolder: Holds references to the views of each item (todo_row.xml)
    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivDelete: ImageView = itemView.findViewById(R.id.ivDelete)
        val cvTask: CardView = itemView.findViewById(R.id.cvTask)
        val titleTextView: TextView = itemView.findViewById(R.id.taskTitle) // [1]
        val timeTextView: TextView = itemView.findViewById(R.id.userdatecalendar)   // [1]
        val completedCheckBox: CheckBox = itemView.findViewById(R.id.checkBox)
    }

    // Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        // LayoutInflater is used to create View objects from XML layout files.
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.todo_row, parent, false) // [1]
        // 2. Create and Return a TaskViewHolder Instance:
        // The inflated view (itemView) is passed to the TaskViewHolder constructor.
        return TaskViewHolder(itemView)

    }

    // Called by RecyclerView to display the data at the specified position.
    // This method should update the contents of the ViewHolder to reflect the item at the given position.
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = tasks[position] // Get the current task from the list

        // Assigns the current task's data to the ViewHolder's views
        holder.titleTextView.text = currentTask.title
        holder.timeTextView.text = currentTask.date // Or any date format you wanta

        // Set a click listener for the delete button
        holder.ivDelete.setOnClickListener {
            tasks.removeAt(holder.adapterPosition)
            notifyItemRemoved(holder.adapterPosition)
        }

        // Listener for the entire CardView (cvTask):
        // This allows clicking anywhere on the task card to perform an action.
        holder.cvTask.setOnClickListener {
            //Intent to start the ListTask activity
            val intent = Intent(context, ListTask::class.java)
            //add the currentTask
            intent.putExtra("task_send", currentTask)
            context.startActivity(intent)
        }
    }

    // Method getItemCount
    //Returns the total number of items in the dataset handled by the adapter.
    override fun getItemCount(): Int {
        return tasks.size
    }

}