package ru.yotc.wearosigimbaev

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.support.wearable.activity.WearableActivity
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import org.json.JSONObject
import java.lang.Exception

class MainActivity : WearableActivity() {

    private lateinit var app: myApp
    var counter = 0
    var ready = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = applicationContext as myApp



        val splash = findViewById<ImageView>(R.id.splash)
        object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                counter++
                if (counter > 3 && ready) {
                    splash.elevation = 0F
                    this.cancel()
                }

            }

            override fun onFinish() {
                splash.elevation = 0F
            }
        }.start()
        onLoginResponce(app.loginText, app.passwordText)
        startActivity(Intent(this, ChatActivity::class.java))

    }
    fun onLoginResponce (login: String, password: String)
    {
        app.username = login
        val json = JSONObject()
        json.put("username", login)
        json.put("password", password)

        HTTP.requestPOST(
            "http://s4a.kolei.ru/login",
            json,
            mapOf(
                "ContentType" to "application/json"
            )
        ) { result, error ->
            if (result != null) {
                try {
                    val jsonResp = JSONObject(result)
                    if (!jsonResp.has("notice"))
                        throw Exception("Не верный формат, ожидался объект notice")
                    if (jsonResp.getJSONObject("notice").has("answer"))
                        throw Exception(jsonResp.getJSONObject("notice").getString("answer"))
                    if (jsonResp.getJSONObject("notice").has("token")) {
                        app.token = jsonResp.getJSONObject("notice").getString("token")
                        runOnUiThread {
                            Toast.makeText(
                                this,
                                "Success get token:$app.token",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                    } else
                        throw Exception("Не верный формат ожидался объект token")

                } catch (e: Exception) {
                    runOnUiThread {
                        AlertDialog.Builder(this)
                            .setTitle("Ошибка")
                            .setMessage(e.message)
                            .setPositiveButton("OK", null)
                            .create()
                            .show()
                    }
                }
            } else
                runOnUiThread {
                    AlertDialog.Builder(this)
                        .setTitle("Ошибка http-запроса")
                        .setMessage(error)
                        .setPositiveButton("OK", null)
                        .create()
                        .show()
                }

        }
    }

}
