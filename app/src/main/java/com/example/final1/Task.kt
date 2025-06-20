package com.example.final1

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


//Its main function is to automatically generate the code necessary to implement the interface
//It is an interface that allows objects to be "flattened" or "serialized" into a format that
// can be efficiently transferred between different application components.
//"Hey, this class needs to be Parcelable. Please generate all that code
// for me based on the properties I've defined in the primary constructor."

//Which means that Task objects can be easily
// packaged and sent between different parts of your Android application.
@Parcelize
data class Task(
    //constructor properties
    val id: String = System.currentTimeMillis().toString(),
    val title: String,
    val description: String?,
    val date: String,
) : Parcelable