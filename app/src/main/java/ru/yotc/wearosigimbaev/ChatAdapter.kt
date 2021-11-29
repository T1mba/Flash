package ru.yotc.wearosigimbaev

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.wear.widget.WearableRecyclerView
import org.w3c.dom.Text
import java.lang.Exception
import java.util.ArrayList
class ChatAdapter (

    private val values: ArrayList<Chat>
): RecyclerView.Adapter<ChatAdapter.ViewHolder>(){
    private var itemClickListener: ((Chat)-> Unit)? = null
    fun setItemClickListener(itemClickListener:(Chat)-> Unit){
        this.itemClickListener = itemClickListener

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder{
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.chat_item,
            parent,
                false
                )
        return ViewHolder(itemView)
    }
    override fun getItemCount(): Int = values.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        holder.userView.text = values[position].user
        holder.messageView.text = values[position].message
    }
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var userView: TextView = itemView.findViewById(R.id.user)
        var messageView: TextView = itemView.findViewById(R.id.message)
    }
}

