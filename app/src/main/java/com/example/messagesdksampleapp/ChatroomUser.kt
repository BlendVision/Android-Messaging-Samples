package com.example.messagesdksampleapp

class ChatroomUser(var username: String = "",
                   var deviceID: String = "",
                   var clientID: String = "",
                   var isAdmin: Boolean = false,
                   var isBlock: Boolean = false,
                   var isConnected: Boolean = false) {
}