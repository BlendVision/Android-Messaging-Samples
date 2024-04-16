package com.example.messagesdksampleapp.viewHolder

import android.view.View
import android.widget.TextView
import com.example.messagesdksampleapp.MessageInfoData
import com.example.messagesdksampleapp.R

class MessageUpdateViewHolder(itemView: View) : MessageBaseViewHolder(itemView) {
    private val textInfoDetail: TextView = itemView.findViewById(R.id.text_update_detail)

    override fun bind(message: MessageInfoData, isAdmin: Boolean) {
        textInfoDetail.text = message.messageInfo.type
    }
}