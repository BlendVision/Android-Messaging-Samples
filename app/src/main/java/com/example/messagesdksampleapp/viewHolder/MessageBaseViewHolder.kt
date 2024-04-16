package com.example.messagesdksampleapp.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.messagesdksampleapp.MessageInfoData

abstract class MessageBaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(message: MessageInfoData, isAdmin: Boolean)
}