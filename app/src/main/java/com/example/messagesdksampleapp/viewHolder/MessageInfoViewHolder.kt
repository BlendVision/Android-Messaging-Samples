package com.example.messagesdksampleapp.viewHolder

import android.view.View
import android.widget.TextView
import com.example.messagesdksampleapp.ChatroomInfo
import com.example.messagesdksampleapp.R

class MessageInfoViewHolder(itemView: View) : MessageInfoBaseViewHolder(itemView) {
    private val textTitle: TextView = itemView.findViewById(R.id.text_title)
    private val textDetail: TextView = itemView.findViewById(R.id.text_detail)

    override fun bind(item: ChatroomInfo) {
        textTitle.text = item.title
        textDetail.text = item.detail
    }
}