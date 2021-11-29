package ru.yotc.wearosigimbaev

import android.app.AlertDialog
import android.app.Application
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import androidx.wear.widget.WearableLinearLayoutManager
import androidx.wear.widget.WearableRecyclerView
import org.json.JSONObject
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.timer
import kotlin.concurrent.timerTask

class ChatActivity : WearableActivity() {
    private val chatList = ArrayList<Chat>()
    private lateinit var app: myApp
    private lateinit var chatInfoRecyclerView: WearableRecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        //setAmbientEnabled()
        // Enables Always-on
        app = applicationContext as myApp
        chatInfoRecyclerView = findViewById(R.id.chatinfo)
        chatInfoRecyclerView.isEdgeItemsCenteringEnabled = true
        chatInfoRecyclerView.layoutManager = WearableLinearLayoutManager(this)
        chatInfoRecyclerView.adapter = ChatAdapter(chatList)
        timer(period = 5000L, startAt = Date()) {
            updateChatlist()


        }
    }
    fun updateChatlist(){
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
                            chatInfoRecyclerView.adapter?.notifyDataSetChanged()
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
        }
    }
}