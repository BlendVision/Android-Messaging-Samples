package com.example.messagesdksampleapp.viewHolder

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blendvision.chat.messaging.common.presentation.Message
import com.example.messagesdksampleapp.listener.PinnedMessageDialogActionListener
import com.example.messagesdksampleapp.R

class PinnedMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val textUsername: TextView = itemView.findViewById(R.id.text_username)
    private val textMessage: TextView = itemView.findViewById(R.id.text_message)
    private val buttonUnpin: Button = itemView.findViewById(R.id.button_unpinned)

    fun bind(item: Message, listener: PinnedMessageDialogActionListener) {
        textUsername.text = item.sender.customName
        textMessage.text = item.text
        buttonUnpin.setOnClickListener {
            listener.onDialogUnpinMessageClicked(item.id)
        }
    }
}