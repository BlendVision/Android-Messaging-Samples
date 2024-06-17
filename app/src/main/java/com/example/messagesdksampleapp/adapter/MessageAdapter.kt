package com.example.messagesdksampleapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.blendvision.chat.messaging.common.presentation.MessageType
import com.example.messagesdksampleapp.listener.MessageActionListener
import com.example.messagesdksampleapp.MessageInfoData
import com.example.messagesdksampleapp.R
import com.example.messagesdksampleapp.ChatroomUser
import com.example.messagesdksampleapp.viewHolder.MessageAdminViewHolder
import com.example.messagesdksampleapp.viewHolder.MessageBaseViewHolder
import com.example.messagesdksampleapp.viewHolder.MessageMyViewHolder
import com.example.messagesdksampleapp.viewHolder.MessageOtherViewHolder
import com.example.messagesdksampleapp.viewHolder.MessageUpdateViewHolder

class MessageAdapter(private val chatroomUser: ChatroomUser, private val listener: MessageActionListener) : RecyclerView.Adapter<MessageBaseViewHolder>() {
    val messages = mutableListOf<MessageInfoData>()
    enum class MessageDisplayType(val num: Int) {
        MY(0),
        ADMIN(1),
        OTHER(2),
        INFO_UPDATE(3)
    }

    override fun getItemViewType(position: Int): Int {

        return if (messages[position].messageInfo.type != MessageType.INTERACTION_TYPE_TEXT.toString()) {
            MessageDisplayType.INFO_UPDATE.num
        } else if (messages[position].messageInfo.user.deviceId == chatroomUser.deviceID) {
            MessageDisplayType.MY.num
        } else if (messages[position].messageInfo.user.isAdmin) {
            MessageDisplayType.ADMIN.num
        } else {
            MessageDisplayType.OTHER.num
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageBaseViewHolder {
        return when(viewType) {
            MessageDisplayType.ADMIN.num -> {
                MessageAdminViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_admin_message, parent, false))
            }
            MessageDisplayType.MY.num -> {
                MessageMyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_my_message, parent, false))
            }
            MessageDisplayType.INFO_UPDATE.num -> {
                MessageUpdateViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_update_info_message, parent, false))
            }
            else -> {
                MessageOtherViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_other_message, parent, false))
            }
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: MessageBaseViewHolder, position: Int) {
        val message = messages[position]

        if (chatroomUser.isAdmin && message.messageInfo.type == MessageType.INTERACTION_TYPE_TEXT.toString()) {
            holder.itemView.setOnLongClickListener { v ->
                val popupMenu = PopupMenu(v.context, v)
                popupMenu.menuInflater.inflate(R.menu.admin_message_action_menu, popupMenu.menu)
                popupMenu.menu.findItem(R.id.message_action_pin).isVisible = !message.isMessagePinned
                popupMenu.menu.findItem(R.id.message_action_unpin).isVisible = message.isMessagePinned
                popupMenu.menu.findItem(R.id.message_action_block_user).isVisible = chatroomUser.deviceID != message.messageInfo.user.deviceId && !message.messageInfo.user.isAdmin && !message.isSenderUserBlocked
                popupMenu.menu.findItem(R.id.message_action_unblock_user).isVisible = chatroomUser.deviceID != message.messageInfo.user.deviceId && !message.messageInfo.user.isAdmin && message.isSenderUserBlocked

                popupMenu.setOnMenuItemClickListener {
                    when(it.itemId) {
                        R.id.message_action_delete -> {
                            listener.onDeleteMessageClicked(message.messageInfo.id, message.messageInfo.user.customName, message.messageInfo.timestampReceivedAt)
                            return@setOnMenuItemClickListener true
                        }
                        R.id.message_action_pin -> {
                            listener.onPinMessageClicked(message.messageInfo.id, message.messageInfo.textMessage?.text.toString(), message.messageInfo.user.id, message.messageInfo.user.deviceId, message.messageInfo.user.customName)
                            return@setOnMenuItemClickListener true
                        }
                        R.id.message_action_unpin -> {
                            listener.onUnpinMessageClicked(message.messageInfo.id)
                            return@setOnMenuItemClickListener true
                        }
                        R.id.message_action_block_user -> {
                            listener.onBlockUserClicked(message.messageInfo.user.id, message.messageInfo.user.deviceId, message.messageInfo.user.customName)
                            return@setOnMenuItemClickListener true
                        }
                        R.id.message_action_unblock_user -> {
                            listener.onUnblockUserClicked(message.messageInfo.user.id)
                            return@setOnMenuItemClickListener true
                        }
                        else -> return@setOnMenuItemClickListener false
                    }
                }
                popupMenu.show()
                return@setOnLongClickListener true
            }
        }
        holder.bind(message, chatroomUser.isAdmin)
    }
}