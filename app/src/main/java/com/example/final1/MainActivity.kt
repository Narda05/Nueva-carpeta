package com.example.final1

import android.app.Activity // Necesario para ActivityResultContracts
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button // Importación añadida
import android.widget.TextView
import android.widget.Toast // Para mostrar un mensaje (opcional)
import androidx.activity.result.contract.ActivityResultContracts // Para el nuevo API de resultados
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
// import androidx.activity.enableEdgeToEdge // Comentado si no lo necesitas explícitamente ahora
// import androidx.core.view.ViewCompat
// import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat // Importación añadida
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date // Importación añadida
import java.util.Locale // Importación añadida
import java.time.format.DateTimeParseException

class MainActivity : AppCompatActivity() {

    private lateinit var addButton: Button

    // Variables for the task list and the RecyclerView
    private val tasksList = mutableListOf<Task>() // Your list of data
    private lateinit var tasksAdapter: TasksAdapter // Nombre de tu adaptador
    private lateinit var recyclerViewTasks: RecyclerView //Name of your adapter

    private val taskDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy") //Formatter to convert date strings (dd/MM/yyyy)


    // 1.Contract Registration for AddNote Activity Results
    // This code block is executed when AddNote calls finish() and returns a result.
    private val addNoteLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result ->
        // 1.2 Check if the operation in AddNote was successful (RESULT_OK).
        if (result.resultCode == Activity.RESULT_OK)
        {
            val data: Intent? = result.data // Get the Intent that AddNote sent back.
            if (data != null) {

                // 2. Extract the Task object from the Intent.
                // The 'AddNote.EXTRA_NEW_TASK_OBJECT' key defined in AddNote is used.
                val receivedTask: Task? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    //Use the AddNote constant to get the Parcelable:
                    data.getParcelableExtra(AddNote.EXTRA_NEW_TASK_OBJECT, Task::class.java) // <--- USANDO LA CONSTANTE DE AddNote
                } else {
                    @Suppress("DEPRECATION")
                    // Use the AddNote constant to get the Parcelable:
                    data.getParcelableExtra<Task>(AddNote.EXTRA_NEW_TASK_OBJECT) // <--- USANDO LA CONSTANTE DE AddNote
                }



                // 3. Process the Received Task.
                if (receivedTask != null)
                {
                    // ÉXITO: tienes el objeto Task
                    Toast.makeText(this, "Task Received: ${receivedTask.title}", Toast.LENGTH_LONG).show()
                    Log.d("MainActivity", "Task Received: ${receivedTask.title} with clave ${AddNote.EXTRA_NEW_TASK_OBJECT}")

                    // 3.1 Add the new task to your data list.
                    tasksList.add(receivedTask)

                    // 3.2 Reorders the entire list by task date, from earliest to latest (Ascending)
                    tasksList.sortBy { task -> // <--- CAMBIO AQUÍ a sortBy
                        try {
                            LocalDate.parse(task.date, taskDateFormatter)
                        } catch (e: DateTimeParseException) {
                            Log.e("MainActivity", "Invalid date format for '${task.title}': ${task.date}. It will be treated as old.", e)
                            LocalDate.MIN // For errors, the earliest is considered
                        } catch (e: Exception) {
                            Log.e("MainActivity", "Unexpected error with date '${task.title}': ${task.date}. It will be treated as old.", e)
                            LocalDate.MIN
                        }
                    }
                    // 3.3 Notify the adapter that the data set has completely changed.
                    tasksAdapter.notifyDataSetChanged()
                }
                else {
                    // ERROR: The Task object could not be extracted or was null.
                    Toast.makeText(this, "Error: Null Task object. Correct key?", Toast.LENGTH_LONG).show()
                    Log.e("MainActivity", "The Task object received from AddNote was null. Key used to obtain: ${AddNote.EXTRA_NEW_TASK_OBJECT}. Extras: ${data.extras}")
                }
            } else {
                //Handle the case where the user canceled adding the note in AddNote.
                Toast.makeText(this, "Error: No se recibieron datos de AddNote (Intent nulo)", Toast.LENGTH_LONG).show()
                Log.e("MainActivity", "The AddNote result intent was null but the resultCode was OK")
            }
        } else if (result.resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(this, "Addition of canceled note", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState) // Always call super.onCreate() first.
        setContentView(R.layout.activity_main) // Inflate the XML layout for this activity.

        // 4. Initialize the Add Task Button
        addButton = findViewById(R.id.addButton)

        addButton.setOnClickListener {
            //4.1 Create an Intent to start the AddNote activity.
            val intent = Intent(this, AddNote::class.java)
            addNoteLauncher.launch(intent)
        }

        // 5. Initialize the RecyclerView and Adapter ---
        // 5.1 Get the reference to the RecyclerView from your layout
        recyclerViewTasks = findViewById(R.id.recyclerViewTasks)

        // 6. Create an instance of your TasksAdapter, passing it the list of tasks.
        tasksAdapter = TasksAdapter(this, tasksList) // tasksList is the property of this class

        // 7.Assign the adapter to the RecyclerView.
        // This tells the RecyclerView how to retrieve and display the data.
        recyclerViewTasks.adapter = tasksAdapter

        // 8. Asigna un LayoutManager al RecyclerView. LinearLayoutManager arranges
        // items in a simple list (vertical or horizontal).
        recyclerViewTasks.layoutManager = LinearLayoutManager(this)

        // 9. If the size of each item in the RecyclerView doesn't change dynamically,
        /// setting setHasFixedSize(true) may improve performance.
        recyclerViewTasks.setHasFixedSize(true)


    }
}