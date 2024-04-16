package com.example.messagesdksampleapp.viewHolder

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blendvision.chat.messaging.common.presentation.BlockedUser
import com.example.messagesdksampleapp.R
import com.example.messagesdksampleapp.listener.BlockedUserDialogActionListener

class BlockedUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val textUsername: TextView = itemView.findViewById(R.id.text_username)
    private val textID: TextView = itemView.findViewById(R.id.text_id)
    private val buttonUnpin: Button = itemView.findViewById(R.id.button_unpinned)

    fun bind(item: BlockedUser, listener: BlockedUserDialogActionListener) {
        textUsername.text = item.customName
        textID.text = item.id
        buttonUnpin.setOnClickListener {
            listener.onDialogUnblockUserClicked(item.id)
        }
    }
}