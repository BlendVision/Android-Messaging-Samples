package com.example.messagesdksampleapp.viewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.messagesdksampleapp.MessageInfoData
import com.example.messagesdksampleapp.R

class MessageMyViewHolder(itemView: View) : MessageBaseViewHolder(itemView) {
    private val textMessage: TextView = itemView.findViewById(R.id.text_message)
    private val textUsername: TextView = itemView.findViewById(R.id.text_username)
    private val viewPin: ImageView = itemView.findViewById(R.id.view_pin)

    override fun bind(message: MessageInfoData, isAdmin: Boolean) {
        textMessage.text = message.messageInfo.textMessage?.text
        textUsername.text = message.messageInfo.user.customName
        viewPin.visibility = if (message.isMessagePinned) View.VISIBLE else View.GONE
    }
}