package com.example.prettylistapp

import android.content.Context
import android.widget.Toast

fun errorToastAlert(message: String, context: Context) {

    val errorToast = Toast.makeText(context, "Error: $message", Toast.LENGTH_SHORT)
    errorToast.show()

}