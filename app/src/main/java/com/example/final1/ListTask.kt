package com.example.final1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text

class ListTask : AppCompatActivity() {

    //Declaring Variables for Views (UI Components)
    private lateinit var tvTitle: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvDate: TextView
    private lateinit var returnToMainButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_task)

        //Return to Main Button
        returnToMainButton = findViewById(R.id.buttonReturnToMain)
        returnToMainButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            //FLAG_ACTIVITY_CLEAR_TOP is important here.
            // If MainActivity is in the stack (but not at the top), this will bring it
            // to the top, clearing whatever is above it.
            // If MainActivity is at the top, singleTop (from the manifest) ensures that
            // it is reused without recreating it.
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        //Initializing TextViews to Display Task Details
        tvTitle = findViewById(R.id.tvTitle)
        tvDescription = findViewById(R.id.tvDescription)
        tvDate = findViewById(R.id.tvDate)

        //Receiving and Displaying Data from a Task:
        val task = intent.getParcelableExtra<Task>("task_send")
        tvTitle.text = task?.title ?: ""
        tvDescription.text = task?.description ?: ""
        tvDate.text = task?.date ?: ""
    }
}