package ru.yotc.wearosigimbaev

import android.app.AlertDialog
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.wear.widget.WearableRecyclerView
import org.json.JSONObject
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.timer

class ChatActivity : WearableActivity() {

    private lateinit var app: myApp
    private val chatList = ArrayList<Chat>()
    private lateinit var message: EditText
    private lateinit var wrc: WearableRecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = applicationContext as myApp

        wrc = findViewById(R.id.chatInfo)
        wrc.isEdgeItemsCenteringEnabled = true
        wrc.layoutManager = object: LinearLayoutManager(this, VERTICAL, false){

            override fun onInterceptFocusSearch(focused: View, direction: Int): View?{
                if(direction == View.FOCUS_DOWN){
                    val pos = getPosition(focused)
                    if(pos == itemCount-1)
                        return focused
                }
                if(direction== View.FOCUS_UP){
                    val pos = getPosition(focused)
                    if(pos == 0)
                        return focused
                }
                return super.onInterceptFocusSearch(focused, direction)
            }
        }
        wrc.adapter = ChatAdapter(chatList, this)
        message = findViewById(R.id.message)
        timer(period = 5000L, startAt = Date()){
            updateChatList()
        }


    }




    fun updateChatList (){
        HTTP.requestGET(
            "http://s4a.kolei.ru/chat",
            mapOf(
                "token" to app.token
            )
        ) { result, error ->
            if (result != null)
                try {
                    chatList.clear()
                    val json = JSONObject(result)
                    if (!json.has("notice"))
                        throw Exception("Не верный формат, ожидался объект notice")
                    if (json.getJSONObject("notice").has("message")) {
                        val mes = json.getJSONObject("notice").getJSONArray("message")
                        for (i in 0 until mes.length()) {
                            val item = mes.getJSONObject(i)
                            chatList.add(
                                Chat(
                                    item.getString("user"),
                                    item.getString("message")
                                )
                            )
                        }
                        runOnUiThread {
                            wrc.adapter?.notifyDataSetChanged()
                        }
                    } else {
                        throw Exception("Не верный формат, ожидался объект token")
                    }
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
            else
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

    fun enterMessage(view: android.view.View) {
        HTTP.requestPOST(
            "http://s4a.kolei.ru/chat",
            JSONObject().put("message",message.text.toString()),
            mapOf(
                "ContentType" to "application/json",
                "Token" to app.token
            )
        ){result, error ->
            if(result!=null){
                try{
                    val json = JSONObject(result)
                    if(!json.has("notice"))
                        throw java.lang.Exception("Не верный формат ответа,ожидался объект notice")
                    if(json.getJSONObject("notice").has("Answer"))
                        throw java.lang.Exception(json.getJSONObject("notice").getString("Answer"))
                }
                catch (e:java.lang.Exception)

                {
                    runOnUiThread {
                        AlertDialog.Builder(this)
                            .setTitle("Ошибка")
                            .setMessage(e.message)
                            .setPositiveButton("OK", null)
                            .create()
                            .show()
                    }
                }

            }
        }
    }
}