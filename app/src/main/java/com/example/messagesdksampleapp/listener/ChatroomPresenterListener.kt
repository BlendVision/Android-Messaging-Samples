package com.example.messagesdksampleapp.listener

import com.blendvision.chat.messaging.common.presentation.CustomCounter
import com.blendvision.chat.messaging.common.presentation.MessageException
import com.blendvision.chat.messaging.common.presentation.MessageInfo
import com.blendvision.chat.messaging.common.presentation.Self
import com.blendvision.chat.messaging.message.presentation.state.ConnectionState

interface ChatroomPresenterListener {
    fun onChatroomConnectionChanged(connectionState: ConnectionState)
//    fun onMuteChatroomSuccess()
//    fun onUnmuteChatroomSuccess()
//    fun onBlockChatRoomUserSuccess(userId: String)
//    fun onUnblockChatRoomUserSuccess(userId: String)
    fun onGetChatHistorySuccess(messages: List<MessageInfo>)
    fun onDeleteMessageSuccess(messageId: String)
//    fun onPinMessageSuccess()
//    fun onUnpinMessageSuccess()
    fun onUpdateViewerInfoSuccess()
    fun onUpdateUserSuccess()
    fun onRefreshTokenSuccess()
    fun onError(exception: MessageException)
    fun onReceiveMessage(messages: List<MessageInfo>)
    fun onCustomCountersInit(customCounters: List<CustomCounter>)
    fun onCustomCountersUpdated(customCounters: List<CustomCounter>)
    fun onCustomMessageCountUpdated(increment: Int, customCounters: CustomCounter)
    fun onGetSelfInfoSuccess(self: Self)
    fun onGetMessagesSuccess(messages: List<MessageInfo>)
}