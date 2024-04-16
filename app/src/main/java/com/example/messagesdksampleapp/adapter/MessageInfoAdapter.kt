package com.example.messagesdksampleapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.messagesdksampleapp.ChatroomInfo
import com.example.messagesdksampleapp.ChatroomInfoType
import com.example.messagesdksampleapp.R
import com.example.messagesdksampleapp.viewHolder.MessageInfoBaseViewHolder
import com.example.messagesdksampleapp.viewHolder.MessageInfoSectionViewHolder
import com.example.messagesdksampleapp.viewHolder.MessageInfoViewHolder

class MessageInfoAdapter : RecyclerView.Adapter<MessageInfoBaseViewHolder>() {
    var data = mutableListOf<ChatroomInfo>()

    override fun getItemViewType(position: Int): Int {
        return data[position].type.num
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageInfoBaseViewHolder {
        return when(viewType) {
            ChatroomInfoType.Section.num -> {
                MessageInfoSectionViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_message_info_section, parent, false))
            }
            else -> {
                MessageInfoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_message_info_text, parent, false))
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MessageInfoBaseViewHolder, position: Int) {
        if (data[position].type == ChatroomInfoType.Text && data[position].detail.isBlank()) {
            holder.itemView.layoutParams = RecyclerView.LayoutParams(0,0)
        } else {
            holder.itemView.layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        holder.bind(data[position])
    }
}