# 📅 Agenda - Task Management Android App

A modern and intuitive task management application built with Android Studio and Kotlin.
![Image](https://github.com/user-attachments/assets/b218acd0-0733-41aa-99d5-96bc3b8e75db)

## Menu
![Image](https://github.com/user-attachments/assets/e45237a2-2d93-4acf-9804-cf819a80b3dc)

## New Task
![Image](https://github.com/user-attachments/assets/4b6c617f-ac64-41e8-a7ef-d493efe87c70)

## Order by dates
![Image](https://github.com/user-attachments/assets/5758192f-b805-4f8b-b55e-fd968c2ad014)



## 🌟 Features

### Core Functionality
- **Create Tasks**: Add new tasks with title, description, and due date
- **View Tasks**: Browse all tasks in an organized list view
- **Task Details**: View complete task information in a dedicated screen
- **Delete Tasks**: Remove completed or unwanted tasks
- **Mark Complete**: Check off completed tasks with checkbox functionality
- **Date Selection**: Interactive calendar picker for setting due dates
- **Smart Sorting**: Tasks automatically sorted by due date (earliest first)

### User Experience
- **Splash Screen**: Professional loading screen with custom animation
- **Input Validation**: Ensures required fields are filled before saving
- **Toast Notifications**: User feedback for all major actions
- **Responsive Design**: Clean CardView-based layout with smooth interactions

## 🏗️ Architecture

### Project Structure
```
com.example.final1/
├── LoadScreen.kt          # Splash screen with loading animation
├── MainActivity.kt        # Main activity with task list
├── AddNote.kt            # Task creation screen
├── ListTask.kt           # Task detail view screen
├── Task.kt               # Task data model (Parcelable)
├── TasksAdapter.kt       # RecyclerView adapter for task list
└── TodoAdapter.kt        # Additional adapter implementation
```

### Key Components

#### Activities
- **LoadScreen**: Entry point with animated splash screen (10-second duration)
- **MainActivity**: Central hub displaying task list with RecyclerView
- **AddNote**: Task creation form with date picker and validation
- **ListTask**: Detailed task view with navigation back to main

#### Data Model
The `Task` data class implements Parcelable for efficient data transfer:

```kotlin
@Parcelize
data class Task(
    val id: String = System.currentTimeMillis().toString(),
    val title: String,
    val description: String?,
    val date: String,
) : Parcelable
```

#### Adapters
- **TasksAdapter**: Handles task list display, deletion, and navigation to details
- **Custom ViewHolder**: Efficiently manages CardView-based task items

## 🚀 Getting Started

### Prerequisites
- Android Studio Arctic Fox or newer
- Android SDK API 21+
- Kotlin support enabled

### Installation
1. Clone the repository:
```bash
git clone [your-repository-url]
```

2. Open the project in Android Studio

3. Sync Gradle files

4. Build and run the project on an emulator or physical device

### Dependencies
The project uses standard Android libraries:
- AndroidX AppCompat
- RecyclerView for list display
- CardView for task item layout
- Glide for image loading (splash screen)

## 📱 User Interface

### Main Screen
- **Floating Action Button**: Quick access to add new tasks
- **RecyclerView**: Scrollable list of all tasks
- **Task Cards**: Each task displayed in a clean card format showing title and date

### Add Task Screen
- **Title Field**: Required text input for task name
- **Description Field**: Optional detailed task description
- **Date Picker**: Calendar popup for selecting due dates
- **Save/Cancel Buttons**: Action buttons with proper validation

### Task Detail Screen
- **Full Task Information**: Complete display of task details
- **Return Navigation**: Easy navigation back to main screen

## 🔧 Technical Implementation

### Data Persistence
The app uses Android's Parcelable interface for efficient data transfer between activities:

```kotlin
// In AddNote.kt - Creating and sending task data
val newTask = Task(
    title = title,
    description = descriptionInput.ifEmpty { null },
    date = actualSelectedDateString,
)

val resultIntent = Intent()
resultIntent.putExtra(EXTRA_NEW_TASK_OBJECT, newTask)
setResult(Activity.RESULT_OK, resultIntent)
```

### Date Handling
Robust date management with validation and formatting:

```kotlin
// Date picker implementation in AddNote.kt
private fun showDatePickerDialog() {
    val year = calendarInstance.get(Calendar.YEAR)
    val month = calendarInstance.get(Calendar.MONTH)
    val day = calendarInstance.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        this,
        { _, selectedYear, selectedMonth, selectedDayOfMonth ->
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.set(selectedYear, selectedMonth, selectedDayOfMonth)
            actualSelectedDateString = displayDateFormat.format(selectedCalendar.time)
            textViewSelectedDate.text = actualSelectedDateString
        },
        year, month, day
    )
    
    // Prevent past date selection
    datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
    datePickerDialog.show()
}
```

### Activity Communication
Modern ActivityResultContracts API for handling results:

```kotlin
// In MainActivity.kt - Handling results from AddNote
private val addNoteLauncher = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()
) { result ->
    if (result.resultCode == Activity.RESULT_OK) {
        val data: Intent? = result.data
        if (data != null) {
            val receivedTask: Task? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                data.getParcelableExtra(AddNote.EXTRA_NEW_TASK_OBJECT, Task::class.java)
            } else {
                @Suppress("DEPRECATION")
                data.getParcelableExtra<Task>(AddNote.EXTRA_NEW_TASK_OBJECT)
            }
            
            if (receivedTask != null) {
                tasksList.add(receivedTask)
                // Sort tasks by date
                tasksList.sortBy { task ->
                    try {
                        LocalDate.parse(task.date, taskDateFormatter)
                    } catch (e: DateTimeParseException) {
                        LocalDate.MIN
                    }
                }
                tasksAdapter.notifyDataSetChanged()
            }
        }
    }
}
```

### RecyclerView Implementation
Efficient list management with ViewHolder pattern:

```kotlin
// TasksAdapter.kt - ViewHolder implementation
inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val ivDelete: ImageView = itemView.findViewById(R.id.ivDelete)
    val cvTask: CardView = itemView.findViewById(R.id.cvTask)
    val titleTextView: TextView = itemView.findViewById(R.id.taskTitle)
    val timeTextView: TextView = itemView.findViewById(R.id.userdatecalendar)
    val completedCheckBox: CheckBox = itemView.findViewById(R.id.checkBox)
}

override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
    val currentTask = tasks[position]
    
    holder.titleTextView.text = currentTask.title
    holder.timeTextView.text = currentTask.date
    
    // Delete functionality
    holder.ivDelete.setOnClickListener {
        tasks.removeAt(holder.adapterPosition)
        notifyItemRemoved(holder.adapterPosition)
    }
    
    // Navigation to task details
    holder.cvTask.setOnClickListener {
        val intent = Intent(context, ListTask::class.java)
        intent.putExtra("task_send", currentTask)
        context.startActivity(intent)
    }
}
```

### Input Validation
Comprehensive validation with user feedback:

```kotlin
// In AddNote.kt - Form validation
private fun processSaveNote() {
    val title = editTextTitle.text.toString().trim()
    val descriptionInput = editTextDescription.text.toString().trim()

    // Title validation
    if (title.isEmpty()) {
        editTextTitle.error = "The title cannot be empty"
        Toast.makeText(this, "Please enter a title", Toast.LENGTH_LONG).show()
        return
    }
    
    // Date validation
    if (actualSelectedDateString.isEmpty()) {
        Toast.makeText(this, "Select a date", Toast.LENGTH_LONG).show()
        return
    }
    
    // Create and save task...
}
```

### Splash Screen Implementation
Professional loading screen with Glide integration:

```kotlin
// LoadScreen.kt - Animated splash screen
class LoadScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_screen)

        val ivGif: ImageView = findViewById(R.id.ivGif)

        // Load animated image with Glide
        Glide.with(this)
            .load(R.drawable.icon_backagendaquinta)
            .into(ivGif)

        loadMain()
    }

    private fun loadMain() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 10100L) // 10.1 second delay
    }
}
```

## 🎨 Design Patterns

- **MVP Architecture**: Clear separation between data, presentation, and logic
- **ViewHolder Pattern**: Efficient RecyclerView implementation
- **Singleton Calendar**: Reusable calendar instance for date operations
- **Companion Objects**: Constants management for Intent extras

```kotlin
// Companion object for constants
companion object {
    const val EXTRA_NEW_TASK_OBJECT = "EXTRA_NEW_TASK_OBJECT_KEY_V1"
}
```

## 🔄 App Flow

1. **App Launch**: LoadScreen displays for 10 seconds with animation
2. **Main Screen**: User sees task list, can tap '+' to add new task
3. **Add Task**: User fills form, selects date, saves task
4. **Return to Main**: New task appears in sorted list
5. **View Details**: Tap any task card to see full details
6. **Task Management**: Delete tasks or mark as complete from main list

## 📄 Code Quality

- **Kotlin Best Practices**: Follows Kotlin coding conventions
- **Memory Efficient**: Proper ViewHolder pattern implementation
- **Error Resilient**: Comprehensive exception handling
- **User Friendly**: Intuitive UI/UX design
- **Maintainable**: Clean code structure with clear separation of concerns

### Code Highlights

**Null Safety**: Extensive use of Kotlin's null safety features
```kotlin
val description = descriptionInput.ifEmpty { null }
tvDescription.text = task?.description ?: ""
```

**Modern Android APIs**: Using latest Android development practices
```kotlin
// Modern activity result handling
private val addNoteLauncher = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()
) { result -> /* handle result */ }
```

**Exception Handling**: Robust error management
```kotlin
tasksList.sortBy { task ->
    try {
        LocalDate.parse(task.date, taskDateFormatter)
    } catch (e: DateTimeParseException) {
        Log.e("MainActivity", "Invalid date format: ${task.date}", e)
        LocalDate.MIN
    }
}
```

## 🤝 Contributing

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is open source and available under the [MIT License](LICENSE).

## 👨‍💻 Developer

Created as a learning project to demonstrate Android development skills including:
- Kotlin programming
- Android Activity lifecycle
- RecyclerView implementation
- Date handling and validation
- Modern Android UI/UX patterns
- Parcelable data transfer
- Activity result handling
- Input validation and error handling

---

*This README provides a comprehensive overview of the Agenda app with detailed code examples. For technical questions or feature requests, please open an issue in the repository.*
