package ru.yotc.wearosigimbaev

import android.app.AlertDialog
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.widget.TextView
import android.widget.Toast
import org.json.JSONObject
import org.w3c.dom.Text
import java.lang.Exception

class registr : WearableActivity() {
    private lateinit var app: myApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registr)

        app = applicationContext as myApp

        val loginText = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.login)

        val passwordText = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.password)

        val loginButton = findViewById<TextView>(R.id.login_button)
        loginButton.setOnClickListener {

            if(loginText.text!!.isNotEmpty() && passwordText.text!!.isNotEmpty())
            {
                    
            }
            else
                AlertDialog.Builder(this)
                    .setTitle("Ошибка")
                    .setMessage("Должны быть введены логин и пароль")
                    .setPositiveButton("OK", null)
                    .create()
                    .show()
        }



    }
}