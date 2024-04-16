package com.example.messagesdksampleapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blendvision.chat.messaging.common.presentation.BlockedUser
import com.example.messagesdksampleapp.R
import com.example.messagesdksampleapp.listener.BlockedUserDialogActionListener
import com.example.messagesdksampleapp.viewHolder.BlockedUserViewHolder

class BlockedUserAdapter(private val listener: BlockedUserDialogActionListener) : RecyclerView.Adapter<BlockedUserViewHolder>() {
    var blockedUser = mutableListOf<BlockedUser>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlockedUserViewHolder {
        return BlockedUserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_blocked_user, parent, false))
    }

    override fun getItemCount(): Int {
        return blockedUser.size
    }

    override fun onBindViewHolder(holder: BlockedUserViewHolder, position: Int) {
        holder.bind(blockedUser[position], listener)
    }
}