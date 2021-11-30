package ru.yotc.wearosigimbaev

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import org.json.JSONObject
import org.w3c.dom.Text
import java.lang.Exception

class registr : WearableActivity() {


    private lateinit var app:myApp
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = applicationContext as myApp



        val loginText = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.login)

        val passwordText = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.password)

        val loginButton = findViewById<Button>(R.id.login_button)
        val logoutButton = findViewById<Button>(R.id.logout_button)
        loginButton.setOnClickListener {

            if(loginText.text!!.isNotEmpty() && passwordText.text!!.isNotEmpty())
            {
                app.loginText = loginText.text.toString()
                app.passwordText = passwordText.text.toString()
                startActivityForResult(Intent(this, ChatActivity::class.java),1)
            }
            else
                AlertDialog.Builder(this)
                    .setTitle("Ошибка")
                    .setMessage("Должны быть введены логин и пароль")
                    .setPositiveButton("OK", null)
                    .create()
                    .show()
        }
        logoutButton.setOnClickListener{
            HTTP.requestPOST(
                "http://s4a.kolei.ru/logout",
                JSONObject().put("username", app.username),
                mapOf("ContentType" to "application/json")
            ){result, error ->
                app.token = ""
                runOnUiThread {
                    if(result != null){
                        Toast.makeText(this, "Logout success!", Toast.LENGTH_LONG).show()
                    }
                    else{
                        AlertDialog.Builder(this)
                            .setTitle("Ошибка")
                            .setMessage("error")
                            .create()
                            .show()
                    }
                }
            }

        }

    }
}