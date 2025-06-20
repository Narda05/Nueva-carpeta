package com.example.final1

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// 1.Pop calendar(POPC),
// 2.Proces save the Note (SVNOTE)
class AddNote :AppCompatActivity() {

    // Lateinit: means that the variable will be initialize a non-null variable before using it
    // (Don't worry about null safety at this point for this particular variable)
    // Declaring Variables for Views (UI Components)
    private lateinit var editTextTitle: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var buttonCalendar: Button
    private lateinit var textViewSelectedDate: TextView    // El TextView show the selected date
    private lateinit var buttonSaveNote: Button
    private lateinit var buttonCancelAndReturnToMain: Button

    //Creates an instance of the Calendar object to get the current date
    // and to help display the DatePickerDialog
    private val calendarInstance: Calendar = Calendar.getInstance()
    private val displayDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) // Format to display the date

    //Variable to store the date the user selects in the DatePickerDialog,
    // as a String. It is initialized as an empty String.
    private var actualSelectedDateString: String = ""

    //A companion object is a singleton object associated with a class.
    companion object {
        // Defines a String constant.
        // It will be used as the "key" when passing the Task object back to MainActivity via an Intent.
        // It's important that this key is unique and that MainActivity uses the same key to
        // retrieve the object.
        // 'const val' means it's a compile-time constant.
        const val EXTRA_NEW_TASK_OBJECT = "EXTRA_NEW_TASK_OBJECT_KEY_V1"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        // Inicializar las vistas con los IDs de tu XML
        editTextTitle = findViewById(R.id.editTextTitle)
        editTextDescription = findViewById(R.id.editTextDescription)
        buttonCalendar = findViewById(R.id.calendar_id)
        textViewSelectedDate = findViewById(R.id.date_id) // TextView next to the calendar button
        buttonSaveNote = findViewById(R.id.buttonSaveNote)

        // CANCELL BUTTON
        buttonSaveNote = findViewById(R.id.buttonSaveNote)
        buttonCancelAndReturnToMain = findViewById(R.id.buttonCancelAndReturnToMain)
        buttonCancelAndReturnToMain.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }


        //  1.(POPC) Press the boton to call the fuction
        buttonCalendar.setOnClickListener {
            showDatePickerDialog()
        }
        // 1.1 (POPC) Press to save the note and save the user selected date
        buttonSaveNote.setOnClickListener {
            processSaveNote()
        }

        // Mostrar hint en el TextView de la fecha si está vacío
        if (textViewSelectedDate.text.isEmpty()) {
            // Tenemos un string resource llamado "Date" (ej. <string name="Date">Selecciona fecha</string>)
            try {
                textViewSelectedDate.hint = getString(R.string.Date)
            } catch (e: Exception) {
                textViewSelectedDate.hint = "Date" // Fallback si el string no existe
            }
        }

    }

    // 2.(POPC) fuction to show the calendar
    private fun showDatePickerDialog()
    {
        //2.1 obtain the date actual date to show in the calendar
        val year = calendarInstance.get(Calendar.YEAR)
        val month = calendarInstance.get(Calendar.MONTH) // MondIndex es 0-indexado (Enero=0, Diciembre=11)
        val day = calendarInstance.get(Calendar.DAY_OF_MONTH)

        //2.(POPC) Create the object POP CALENDAR
        val datePickerDialog = DatePickerDialog(
            this,// Context: this activity (AddNote)

            //3.(POPC) ejecuted a lambda fuction and the
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                // Cuando el usuario selecciona una fecha
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(selectedYear, selectedMonth, selectedDayOfMonth)
                actualSelectedDateString = displayDateFormat.format(selectedCalendar.time)
                textViewSelectedDate.text = actualSelectedDateString // 3.3Show tht dathe in the TextView
            },//2.2 Current date that the DataPickerDialog is gona show
            year,
            month,
            day
        )
        // 2.3  Set a minimum date if you don't want past dates to be selected.
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    //1.(SVNOTE)
    private fun processSaveNote()
    {
        //1.1 Obtain user data
        val title = editTextTitle.text.toString().trim()
        val descriptionInput = editTextDescription.text.toString().trim()

        //1.2 Validation Title is required
        if (title.isEmpty()) {
            editTextTitle.error = "The title cannot be empty"
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_LONG).show()
            return //Stops the function execution if the title is empty
        }
        // 1.3 Validation Date is required
        if (actualSelectedDateString.isEmpty()) {
            Toast.makeText(this, "Select a date", Toast.LENGTH_LONG).show()
            return//Stops the function execution if the date is empty
        }

        // 2.(SVNOTE) Crear el objeto Task
        // instantiate a new object in the Task class ()
        val newTask = Task(
            // The 'id' is automatically generated in the Task class with System.currentTimeMillis().toString()
            title = title,
            description = descriptionInput.ifEmpty { null }, // Save null if the description is empty
            date = actualSelectedDateString,
        )

        // 2.(SVNOTE) Prepare the Intent to return the result
        //Preparing the Result to Return to MainActivity: AddNote was started from MainActivity
        //  with the expectation that it could return a result (the new task).
        // This is done using an Intent.
        val resultIntent = Intent()
        // Adjuntar el objeto Task completo. Como Task es Parcelable
        //Parcealble is a Interface in Android that allows objects and their data to be "flattened" or "serialized"
        //Pack object (box)->sedet sent to another location unpacked and rebuilt at destination.
        resultIntent.putExtra(EXTRA_NEW_TASK_OBJECT, newTask)
        // Set the result to OK and attach the Intent with the data
        setResult(Activity.RESULT_OK, resultIntent)

        // Show a message to the user that the note is sabed
        Toast.makeText(this, "Nota '${newTask.title}' Saved", Toast.LENGTH_SHORT).show()
        // Close this activity and return to the previous one (MainActivity)
        finish()
    }

}


