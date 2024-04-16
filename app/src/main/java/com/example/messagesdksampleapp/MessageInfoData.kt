package com.example.messagesdksampleapp

import com.blendvision.chat.messaging.common.presentation.MessageInfo
data class MessageInfoData(val messageInfo: MessageInfo,
                           var isSenderUserBlocked: Boolean = false,
                           var isMessagePinned: Boolean = false) {
}