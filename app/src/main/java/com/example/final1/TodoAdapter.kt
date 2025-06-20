package com.example.final1
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// 1.Definition of the Data Model Class: TodoModel
data class TodoModel(
    val title: String,
    val date: String,  // formato "yyyy-MM-dd"
    val time: String
)

// 1.Definition of the Data Model Class: TodoModel
// Inherits from RecyclerView.Adapter and specifies that it
// will use a ViewHolder of type TodoAdapter.TodoViewHolder.

class TodoAdapter(private val todoList: List<TodoModel>) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {
    //Maintains references to individual views within each item's layout (todo_row.xml)
    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val titleText: TextView = itemView.findViewById(R.id.taskTitle)
            val timeText: TextView = itemView.findViewById(R.id.userdatecalendar)
    }

    // 2. onCreateViewHolder Method
    // Called when the RecyclerView needs to create a new ViewHolder.
    // Occurs at startup and when views are recycled.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_row, parent, false)
            return TodoViewHolder(view)
    }

    // 3. onBindViewHolder Method
    // Called to display data for a specific item in a given ViewHolder.
    // Binds the data from the TodoModel object at position to the views in the holder.
    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
            val task = todoList[position]
            holder.titleText.text = task.title
            holder.timeText.text = task.time
    }

    // 4. getItemCount Method. Returns the total number of items in the dataset.
    override fun getItemCount(): Int = todoList.size
}
