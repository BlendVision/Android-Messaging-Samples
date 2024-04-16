package com.example.messagesdksampleapp

enum class ChatroomInfoType(val num: Int) {
    Text(0),
    Section(1),
}

class ChatroomInfo(var title: String = "",
                   var detail: String = "",
                   var type: ChatroomInfoType = ChatroomInfoType.Text) {
}