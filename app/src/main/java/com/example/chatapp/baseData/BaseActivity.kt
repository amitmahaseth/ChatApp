package com.example.chatapp.baseData

import android.app.ProgressDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity:AppCompatActivity() {
    private lateinit var progressDialog: ProgressDialog

    fun showProgressDialog() {
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Application is loading")
        progressDialog.show()
    }
    fun hideProgressDialog()
    {
        if(progressDialog!=null)
        {
            progressDialog.dismiss()
        }
    }
    fun isLoggedIn(): Boolean {
        val preferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
        //The false represents the default value, if the variable is not stored
        return preferences.getBoolean("isLoggedIn", false)
    }

    fun saveLoggedIn(value: Boolean) {
        val preferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean("isLoggedIn", value)
        editor.commit()

    }
}