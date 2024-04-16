package com.example.messagesdksampleapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blendvision.chat.messaging.common.presentation.Message
import com.example.messagesdksampleapp.listener.PinnedMessageDialogActionListener
import com.example.messagesdksampleapp.R
import com.example.messagesdksampleapp.viewHolder.PinnedMessageViewHolder

class PinnedMessageAdapter(private val listener: PinnedMessageDialogActionListener) : RecyclerView.Adapter<PinnedMessageViewHolder>() {
    var pinnedMessage = mutableListOf<Message>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PinnedMessageViewHolder {
        return PinnedMessageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_pinned_message, parent, false))
    }

    override fun getItemCount(): Int {
        return pinnedMessage.size
    }

    override fun onBindViewHolder(holder: PinnedMessageViewHolder, position: Int) {
        holder.bind(pinnedMessage[position], listener)
    }
}