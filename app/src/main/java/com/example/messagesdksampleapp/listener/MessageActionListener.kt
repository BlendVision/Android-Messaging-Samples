package com.example.messagesdksampleapp.listener

interface MessageActionListener {
    fun onDeleteMessageClicked(messageID: String, userCustomName: String, timestampReceivedAt: String?)
    fun onPinMessageClicked(messageID: String, text: String, userID: String, userDeviceID: String, userCustomName: String)
    fun onUnpinMessageClicked(messageID: String)
    fun onBlockUserClicked(userID: String, userDeviceID: String, userCustomName: String)
    fun onUnblockUserClicked(userID: String)
}