package com.example.messagesdksampleapp.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.messagesdksampleapp.ChatroomInfo

abstract class MessageInfoBaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: ChatroomInfo)
}