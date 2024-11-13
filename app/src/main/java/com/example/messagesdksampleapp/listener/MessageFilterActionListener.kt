package com.example.messagesdksampleapp.listener

import com.blendvision.chat.messaging.common.presentation.MessageType

interface MessageFilterActionListener {
    fun onFilterButtonClicked(types: Set<MessageType>?)
}