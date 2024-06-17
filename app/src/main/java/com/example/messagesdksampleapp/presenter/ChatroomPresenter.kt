package com.example.messagesdksampleapp.presenter

import android.icu.text.SimpleDateFormat
import com.blendvision.chat.messaging.common.presentation.CustomCounter
import com.blendvision.chat.messaging.common.presentation.MessageException
import com.blendvision.chat.messaging.common.presentation.MessageInfo
import com.blendvision.chat.messaging.message.presentation.BVMessageManager
import com.blendvision.chat.messaging.message.presentation.callback.EventListener
import com.blendvision.chat.messaging.message.presentation.callback.MessageListener
import com.blendvision.chat.messaging.message.presentation.state.ConnectionState
import com.example.messagesdksampleapp.listener.ChatroomPresenterListener
import java.util.Date
import java.util.Locale

class ChatroomPresenter(
    chatRoomToken: String,
    refreshToken: String? = null,
    updateInterval: Long? = null,
    batchInterval: Long? = null
) : MessageListener, EventListener {
    private var listener: ChatroomPresenterListener? = null
    var connectionState: ConnectionState = ConnectionState.CONNECTING
    private var messageManager = BVMessageManager.Builder(chatRoomToken, refreshToken)
        .setEventListener(this)
        .setMessageListener(this)
        .setUpdateCustomMessageCountInterval(updateInterval ?: 2000L)
        .setBatchCountableCustomInterval(batchInterval ?: 5000L)
        .build()

    fun bind(listener: ChatroomPresenterListener) {
        this.listener = listener
    }

    fun unbind() {
        this.listener = null
    }

    fun connectChatroom() {
        messageManager.connect()
    }

    fun disconnectChatroom() {
        messageManager.disconnect()
    }

    fun getHistoryMessages() {
        val current = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(Date())
        messageManager.getChatHistory(current, 10)
    }

    fun muteChatroom() {
        messageManager.muteChatRoom()
    }

    fun unmuteChatroom() {
        messageManager.unmuteChatRoom()
    }

    fun sendMessage(message: String) {
        messageManager.publishMessage(message)
    }

    fun deleteMessage(messageID: String, userCustomName: String, timestampReceivedAt: String?) {
        messageManager.deleteMessage(messageID, userCustomName, timestampReceivedAt)
    }

    fun sendCustomMessage(customMessage: String) {
        messageManager.publishCustomMessage(customMessage)
    }

    fun sendCountableCustomMessage(key: String, value: String? = null) {
        messageManager.publishCountableCustomMessage(key, value)
    }

    fun pinMessage(messageID: String, text: String, userID: String, userDeviceID: String, userCustomName: String) {
        messageManager.pinMessage(messageID, text, userID, userDeviceID, userCustomName)
    }

    fun unpinMessage(messageID: String) {
        messageManager.unpinMessage(messageID)
    }

    fun blockUser(userID: String, userDeviceID: String, userCustomName: String) {
        messageManager.blockUser(userID, userDeviceID, userCustomName)
    }

    fun unblockUser(userID: String) {
        messageManager.unblockUser(userID)
    }

    fun updateViewerInfo(enabled: Boolean, customName: String) {
        messageManager.updateViewerInfo(enabled, customName)
    }

    fun updateUser(customName: String) {
        messageManager.updateUser(customName)
    }

    fun refreshToken() {
        messageManager.refreshToken()
    }

    override fun onChatRoomConnectionChanged(connectionState: ConnectionState) {
        this.connectionState = connectionState
        listener?.onChatroomConnectionChanged(connectionState)
    }

    override fun onGetChatHistorySuccess(messages: List<MessageInfo>) {
        listener?.onGetChatHistorySuccess(messages)
    }

    override fun onDeleteMessageSuccess(messageId: String) {
        listener?.onDeleteMessageSuccess(messageId)
    }

    override fun onUpdateViewerInfoSuccess() {
        listener?.onUpdateViewerInfoSuccess()
    }

    override fun onUpdateUserSuccess() {
        listener?.onUpdateUserSuccess()
    }

    override fun onRefreshTokenSuccess() {
        listener?.onRefreshTokenSuccess()
    }

    override fun onError(exception: MessageException) {
        listener?.onError(exception)
    }

    override fun onReceiveMessage(messages: List<MessageInfo>) {
        listener?.onReceiveMessage(messages)
    }

    override fun onCustomCountersInit(customCounters: List<CustomCounter>) {
        listener?.onCustomCountersInit(customCounters)
    }

    override fun onCustomCountersUpdated(customCounters: List<CustomCounter>) {
        listener?.onCustomCountersUpdated(customCounters)
    }

    override fun onCustomMessageCountUpdated(increment: Int, customCounters: CustomCounter) {
        listener?.onCustomMessageCountUpdated(increment, customCounters)
    }
}