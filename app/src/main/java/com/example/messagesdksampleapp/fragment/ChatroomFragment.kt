package com.example.messagesdksampleapp.fragment

import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blendvision.chat.messaging.common.presentation.BlockedUser
import com.blendvision.chat.messaging.common.presentation.CustomCounter
import com.blendvision.chat.messaging.common.presentation.Message
import com.blendvision.chat.messaging.common.presentation.MessageException
import com.blendvision.chat.messaging.common.presentation.MessageInfo
import com.blendvision.chat.messaging.common.presentation.MessageType
import com.blendvision.chat.messaging.common.presentation.TextMessage
import com.blendvision.chat.messaging.common.presentation.User
import com.blendvision.chat.messaging.message.presentation.state.ConnectionState
import com.example.messagesdksampleapp.ChatroomInfo
import com.example.messagesdksampleapp.ChatroomInfoType
import com.example.messagesdksampleapp.ChatroomUser
import com.example.messagesdksampleapp.MessageInfoData
import com.example.messagesdksampleapp.R
import com.example.messagesdksampleapp.activity.MainActivity
import com.example.messagesdksampleapp.adapter.MessageAdapter
import com.example.messagesdksampleapp.listener.BlockedUserDialogActionListener
import com.example.messagesdksampleapp.listener.ChatroomInfoActionListener
import com.example.messagesdksampleapp.listener.ChatroomPresenterListener
import com.example.messagesdksampleapp.listener.MessageActionListener
import com.example.messagesdksampleapp.listener.PinnedMessageDialogActionListener
import com.example.messagesdksampleapp.presenter.ChatroomPresenter
import com.google.android.material.textfield.TextInputEditText
import java.sql.Date
import java.sql.Timestamp
import java.util.Locale

private const val FRAGMENT: String = "ChatroomFragment"
private const val CUSTOM_MESSAGE_KEY_LIKE = "LIKE"
class ChatroomFragment(chatRoomToken: String,
                       private val refreshToken: String? = null, updateInterval: Long?, batchInterval: Long?) : Fragment(), ChatroomPresenterListener,
    MessageActionListener, ChatroomInfoActionListener, PinnedMessageDialogActionListener, BlockedUserDialogActionListener {
    private var chatroomID = ""
    private val chatroomUser = ChatroomUser()
    private var textConnectionState: TextView? = null
    private var textNoHistoryMessage: TextView? = null
    private var recyclerView: RecyclerView? = null
    private var inputTextField: TextInputEditText? = null
    private var buttonSend: Button? = null
    private var buttonUnlike: Button? = null
    private var buttonLike: Button? = null
    private var tvLikeCounter: TextView? = null
    private var buttonDisconnect: Button? = null
    private var buttonInfo: Button? = null
    private var viewLoading: View? = null
    private var updateViewerInfoDialogFragment: UpdateViewerInfoDialogFragment? = null
    private var pinnedMessageDialogFragment: PinnedMessageDialogFragment? = null
    private var blockedUserDialogFragment: BlockedUserDialogFragment? = null
    private var errorMessageDialogFragment: ErrorMessageDialogFragment? = null
    private val presenter = ChatroomPresenter(chatRoomToken, refreshToken, updateInterval, batchInterval)
    private val adapter = MessageAdapter(chatroomUser, this)
    private val blockedUsers = mutableListOf<BlockedUser>()
    private val pinnedMessages = mutableListOf<Message>()
    private var chatroomIsMuted: Boolean = false
    private var viewerInfoEnable: Boolean = false
    private var viewerInfoHasUpdated: Boolean = false
    private var chatroomFirstConnect: Boolean = true
    private var chatroomInfoList: MutableList<ChatroomInfo> = arrayListOf(
        ChatroomInfo("User", "", ChatroomInfoType.Section),
        ChatroomInfo("Name", chatroomUser.username),
        ChatroomInfo("User ID", chatroomUser.clientID),
        ChatroomInfo("Device ID", chatroomUser.deviceID),
        ChatroomInfo("Role", if (chatroomUser.isAdmin) "Admin" else "Viewer"),
        ChatroomInfo("Info", "",  ChatroomInfoType.Section),
        ChatroomInfo("Chatroom ID", chatroomID),
        ChatroomInfo("Viewer Info Enable", ""),
        ChatroomInfo("Viewer Count", ""),
        ChatroomInfo("Viewer Version", ""),
        ChatroomInfo("Update At", "")
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chatroom, container, false)
        textConnectionState = view?.findViewById(R.id.text_connection_state)
        textNoHistoryMessage = view?.findViewById(R.id.text_no_history_message)
        recyclerView = view?.findViewById(R.id.recycler_message)
        inputTextField = view?.findViewById(R.id.input)
        buttonSend = view?.findViewById(R.id.button_send)
        buttonUnlike = view?.findViewById(R.id.button_un_like)
        buttonLike = view?.findViewById(R.id.button_like)
        tvLikeCounter = view?.findViewById(R.id.textview_like_counter)
        buttonDisconnect = view?.findViewById(R.id.button_disconnect)
        buttonInfo = view?.findViewById(R.id.button_info)
        viewLoading = view?.findViewById(R.id.view_loading)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        inputTextField?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                recyclerView?.scrollToPosition(adapter.itemCount - 1)
            }
        }
        buttonDisconnect?.setOnClickListener {
            presenter.disconnectChatroom()
        }
        buttonInfo?.setOnClickListener { v ->
            val popupMenu = PopupMenu(v.context, v)
            popupMenu.menuInflater.inflate(R.menu.chatroom_info_action_menu, popupMenu.menu)
            popupMenu.menu.findItem(R.id.update_user).isVisible = chatroomUser.isAdmin
            popupMenu.menu.findItem(R.id.chatroom_mute).isVisible = chatroomUser.isAdmin && !chatroomIsMuted
            popupMenu.menu.findItem(R.id.chatroom_unmute).isVisible = chatroomUser.isAdmin && chatroomIsMuted
            popupMenu.menu.findItem(R.id.update_viewer_info).isVisible = chatroomUser.isAdmin
            popupMenu.menu.findItem(R.id.update_viewer_info).isEnabled = viewerInfoHasUpdated
            popupMenu.menu.findItem(R.id.chatroom_blocked_user).isVisible = chatroomUser.isAdmin && blockedUsers.isNotEmpty()
            popupMenu.menu.findItem(R.id.chatroom_pinned_message).isVisible = chatroomUser.isAdmin && pinnedMessages.isNotEmpty()
            popupMenu.menu.findItem(R.id.refresh_token).isVisible = !refreshToken.isNullOrEmpty()

            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.refresh_token -> {
                        presenter.refreshToken()
                        return@setOnMenuItemClickListener true
                    }
                    R.id.chatroom_info -> {
                        (activity as MainActivity?)?.replaceFragments(
                            this, ChatroomInfoFragment(this, chatroomInfoList), true
                        )
                        return@setOnMenuItemClickListener true
                    }

                    R.id.update_user -> {
                        presenter.updateUser(chatroomUser.username)
                        return@setOnMenuItemClickListener true
                    }

                    R.id.update_viewer_info -> {
                        updateViewerInfoDialogFragment = UpdateViewerInfoDialogFragment(this, viewerInfoEnable)
                        updateViewerInfoDialogFragment?.show(
                            parentFragmentManager, "Update Viewer Info Dialog"
                        )
                        return@setOnMenuItemClickListener true
                    }

                    R.id.chatroom_mute -> {
                        presenter.muteChatroom()
                        return@setOnMenuItemClickListener true
                    }

                    R.id.chatroom_unmute -> {
                        presenter.unmuteChatroom()
                        return@setOnMenuItemClickListener true
                    }

                    R.id.chatroom_pinned_message -> {
                        pinnedMessageDialogFragment = PinnedMessageDialogFragment(this, pinnedMessages)
                        pinnedMessageDialogFragment?.show(
                            parentFragmentManager, "Pinned Message Dialog"
                        )

                        return@setOnMenuItemClickListener true
                    }

                    R.id.chatroom_blocked_user -> {
                        blockedUserDialogFragment = BlockedUserDialogFragment(this, blockedUsers)
                        blockedUserDialogFragment?.show(
                            parentFragmentManager, "Blocked User Dialog"
                        )

                        return@setOnMenuItemClickListener true
                    }

                    else -> return@setOnMenuItemClickListener false
                }
            }
            popupMenu.show()

            return@setOnClickListener
        }
        buttonSend?.setOnClickListener {
            if (inputTextField?.text.isNullOrBlank().not()) {
                presenter.sendMessage(inputTextField?.text.toString())
            }
            if (chatroomUser.isBlock) {
                val sendTime = System.currentTimeMillis() / 1000
                adapter.messages.add(MessageInfoData(
                    MessageInfo("",
                        User(chatroomUser.clientID, chatroomUser.deviceID, chatroomUser.username, chatroomUser.isAdmin, true),
                        MessageType.INTERACTION_TYPE_TEXT.toString(),
                        TextMessage(inputTextField?.text.toString(), false),
                        null, null,null, null, null, null, null,
                        "", "", "", "",
                        0L, sendTime, 0L, 0L),
                    true))
                adapter.notifyItemChanged(adapter.itemCount - 1)
                recyclerView?.scrollToPosition(adapter.itemCount - 1)
            }
            inputTextField?.text?.clear()
            inputTextField?.clearFocus()
        }

        buttonUnlike?.setOnClickListener {
            presenter.sendCustomMessage("UNLIKE")
        }

        buttonLike?.setOnClickListener {
            presenter.sendCountableCustomMessage(CUSTOM_MESSAGE_KEY_LIKE, "CUSTOM MESSAGE")
        }
        presenter.bind(this)
    }

    override fun onResume() {
        super.onResume()
        if (presenter.connectionState !is ConnectionState.CONNECTED) {
            viewLoading?.visibility = View.VISIBLE
            textNoHistoryMessage?.visibility = View.GONE
            inputTextField?.isEnabled = false
            buttonSend?.isEnabled = false
            context?.let {
                inputTextField?.setHint(R.string.disable_send_message_hint)
            }
            presenter.disconnectChatroom()
            presenter.connectChatroom()
        }
    }

    override fun onDestroy() {
        presenter.unbind()
        presenter.disconnectChatroom()
        super.onDestroy()
    }

    /* MessageListener */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceiveMessage(messages: List<MessageInfo>) {
        val name = "onReceiveMessage"
        Log.d(FRAGMENT, "$name ------------------------------------------------------------")
        Log.d(FRAGMENT, "$name ${messages.size}")

        messages.forEach { message ->
            when (message.type) {
                MessageType.INTERACTION_TYPE_CUSTOM.toString() -> {
                    val isNotCountableLike = message.customMessage?.increment == null && message.customMessage?.value != CUSTOM_MESSAGE_KEY_LIKE
                    val isCountableLike = message.customMessage?.increment?.key == CUSTOM_MESSAGE_KEY_LIKE

                    if (isNotCountableLike) {
                        toast("${message.user.customName} Click Unlike", Toast.LENGTH_SHORT)
                    } else if (isCountableLike) {
                        message.customMessage?.value?.let { customMessage ->
                            toast(
                                "${message.user.customName} Click Countable Like, Custom message: $customMessage",
                                Toast.LENGTH_SHORT
                            )
                        } ?: toast("${message.user.customName} Click Countable Like", Toast.LENGTH_SHORT)
                    } else {
                        toast(
                            "${message.user.customName} Send Other Custom Message",
                            Toast.LENGTH_SHORT
                        )
                    }
                }
                MessageType.INTERACTION_TYPE_ENTRANCE.toString() -> {
                    toast("${message.entranceMessage?.customName} Entrance Chatroom", Toast.LENGTH_SHORT)
                }
                MessageType.INTERACTION_TYPE_DELETE_MESSAGE.toString() -> {
                    val deleteMessageIndex = adapter.messages.indexOfFirst { it.messageInfo.id == message.deleteMessage!!.id }
                    if (deleteMessageIndex != -1) {
                        adapter.messages.removeAt(deleteMessageIndex)
                        adapter.notifyItemChanged(deleteMessageIndex)
                    }
                }
                MessageType.INTERACTION_TYPE_BLOCK_USER.toString() -> {
                    message.blockUnblockUser?.let { blockUser ->
                        blockedUsers.add(BlockedUser(blockUser.actionTaker, blockUser.customName, blockUser.deviceId, blockUser.id))
                        if (blockUser.deviceId == chatroomUser.deviceID) {
                            chatroomUser.isBlock = true
                        }
                        adapter.messages.forEach {
                            if (it.messageInfo.user.id == blockUser.id) {
                                it.isSenderUserBlocked = true
                                adapter.notifyItemChanged(adapter.messages.indexOf(it))
                            }
                        }
                    }
                }
                MessageType.INTERACTION_TYPE_UNBLOCK_USER.toString() -> {
                    message.blockUnblockUser?.let { blockUser ->
                        val index = blockedUsers.indexOfFirst { it.deviceId == blockUser.deviceId }
                        blockedUsers.removeAt(index)
                        if (blockUser.deviceId == chatroomUser.deviceID) {
                            chatroomUser.isBlock = false
                        }
                        adapter.messages.forEach {
                            if (it.messageInfo.user.id == blockUser.id) {
                                it.isSenderUserBlocked = false
                                adapter.notifyItemChanged(adapter.messages.indexOf(it))
                            }
                        }
                        if (blockedUserDialogFragment?.showsDialog == true) {
                            blockedUserDialogFragment?.refreshBlockedUser(index)
                        }
                    }
                }
                MessageType.INTERACTION_TYPE_PIN_MESSAGE.toString() -> {
                    message.pinUnpinMessage?.let { pinMessage ->
                        pinnedMessages.add(pinMessage.message)
                        adapter.messages.forEach {
                            if (it.messageInfo.id == pinMessage.message.id) {
                                it.isMessagePinned = true
                                adapter.notifyItemChanged(adapter.messages.indexOf(it))
                            }
                        }
                    }
                }
                MessageType.INTERACTION_TYPE_UNPIN_MESSAGE.toString() -> {
                    message.pinUnpinMessage?.let { pinMessage ->
                        val index = pinnedMessages.indexOfFirst { it.id == pinMessage.message.id }
                        pinnedMessages.removeAt(index)
                        adapter.messages.forEach {
                            if (it.messageInfo.id == pinMessage.message.id) {
                                it.isMessagePinned = false
                                adapter.notifyItemChanged(adapter.messages.indexOf(it))
                            }
                        }
                        if (pinnedMessageDialogFragment?.showsDialog == true) {
                            pinnedMessageDialogFragment?.refreshPinnedMessage(index)
                        }
                    }
                }
                MessageType.INTERACTION_TYPE_MUTE.toString() -> {
                    chatroomIsMuted = true
                    if (chatroomUser.isAdmin.not()) {
                        inputTextField?.isEnabled = false
                        buttonSend?.isEnabled = false
                        buttonUnlike?.isEnabled = false
                        buttonLike?.isEnabled = false
                        context?.let {
                            inputTextField?.setHint(R.string.chatroom_is_muted_hint)
                        }
                    }
                }
                MessageType.INTERACTION_TYPE_UNMUTE.toString() -> {
                    chatroomIsMuted = false
                    inputTextField?.isEnabled = true
                    buttonSend?.isEnabled = true
                    buttonUnlike?.isEnabled = true
                    buttonLike?.isEnabled = true
                    context?.let {
                        inputTextField?.setHint(R.string.message_to_send_hint)
                    }
                }
                MessageType.INTERACTION_TYPE_VIEWER_INFO_ENABLED.toString() -> {
                    viewerInfoEnable = true
                }
                MessageType.INTERACTION_TYPE_VIEWER_INFO_DISABLED.toString() -> {
                    viewerInfoEnable = false
                }
                MessageType.INTERACTION_TYPE_VIEWER_INFO_UPDATE.toString() -> {
                    viewerInfoHasUpdated = true
                    viewerInfoEnable = message.viewerInfo?.enabled == true
                    chatroomInfoList.first { it.title == "Name" }.detail = chatroomUser.username
                    chatroomInfoList.first { it.title == "User ID" }.detail = chatroomUser.clientID
                    chatroomInfoList.first { it.title == "Device ID" }.detail = chatroomUser.deviceID
                    chatroomInfoList.first { it.title == "Role" }.detail = if (chatroomUser.isAdmin) "Admin" else "Viewer"
                    chatroomInfoList.first { it.title == "Viewer Info Enable" }.detail = (message.viewerInfo?.enabled == true).toString()
                    chatroomInfoList.first { it.title == "Viewer Count" }.detail = message.viewerInfo?.count.toString()
                    chatroomInfoList.first { it.title == "Viewer Version" }.detail = message.viewerInfo?.versionNumber.toString()
                    message.viewerInfo?.unixTimestampUpdatedAt?.let { timestamp ->
                        chatroomInfoList.first { it.title == "Update At" }.detail = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(Timestamp(timestamp * 1000).time))
                    }
                }
            }
            adapter.messages.add(MessageInfoData(message, message.user.blocked))

            Log.d(FRAGMENT, "$name ${message.id}")
            Log.d(FRAGMENT, "$name ${message.textMessage}")
            Log.d(FRAGMENT, "$name ${message.type}")
            Log.d(FRAGMENT, "$name ${message.user.id}, ${message.user.deviceId}, ${messages[0].user.customName}, ${messages[0].user.isAdmin}, ${messages[0].user.blocked}")
            Log.d(FRAGMENT, "$name ${message.blockUnblockUser}")
            Log.d(FRAGMENT, "$name ${message.deleteMessage}")
            Log.d(FRAGMENT, "$name ${message.pinUnpinMessage}")
            Log.d(FRAGMENT, "$name ${message.customMessage}")
            Log.d(FRAGMENT, "$name ${message.timestampSentAt}")
            Log.d(FRAGMENT, "$name ${message.unixTimestampSentAt}")
            Log.d(FRAGMENT, "$name ${message.viewerInfo?.count}")
            Log.d(FRAGMENT, "$name -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -")
        }

        adapter.messages.sortBy { it.messageInfo.timestampSentAt }
        adapter.notifyItemRangeChanged(0, adapter.itemCount)
        recyclerView?.scrollToPosition(adapter.itemCount - 1)

        if (adapter.messages.isEmpty()) {
            textNoHistoryMessage?.visibility = View.VISIBLE
        } else {
            textNoHistoryMessage?.visibility = View.GONE
        }
    }

    /* EventListener */
    override fun onChatroomConnectionChanged(connectionState: ConnectionState) {
        textConnectionState?.text = connectionState.name
        Log.d(FRAGMENT, connectionState.toString())
        when (connectionState) {
            is ConnectionState.DISCONNECTED -> {
                chatroomUser.isConnected = false
                (activity as MainActivity?)?.replaceFragments(this, StartChatroomFragment())
            }

            is ConnectionState.CONNECTED -> {
                chatroomUser.username = connectionState.chatRoomInfo.self.customName
                chatroomUser.clientID = connectionState.chatRoomInfo.self.customerId
                chatroomUser.deviceID = connectionState.chatRoomInfo.self.deviceId
                chatroomUser.isAdmin = connectionState.chatRoomInfo.self.isAdmin
                chatroomUser.isBlock = connectionState.chatRoomInfo.blockedUsers.indexOfFirst { it.deviceId == chatroomUser.deviceID } != -1
                chatroomIsMuted = connectionState.chatRoomInfo.muted
                chatroomID = connectionState.chatRoomInfo.id

                connectionState.chatRoomInfo.blockedUsers.forEach {
                    blockedUsers.add(it)
                }

                connectionState.chatRoomInfo.pinnedMessages.forEach {
                    pinnedMessages.add(it.message)
                }

                chatroomInfoList.first { it.title == "Name" }.detail = chatroomUser.username
                chatroomInfoList.first { it.title == "User ID" }.detail = chatroomUser.clientID
                chatroomInfoList.first { it.title == "Device ID" }.detail = chatroomUser.deviceID
                chatroomInfoList.first { it.title == "Role" }.detail = if (chatroomUser.isAdmin) "Admin" else "Viewer"

                if (chatroomFirstConnect) {
                    presenter.getHistoryMessages()
                    chatroomFirstConnect = false
                }
                if (chatroomUser.isAdmin) {
                    presenter.updateUser(chatroomUser.username)
                }
            }

            else -> {}
        }
    }

    override fun onGetChatHistorySuccess(messages: List<MessageInfo>) {
        Log.d(FRAGMENT, "onGetChatHistorySuccess")
        val beforeIndex = adapter.messages.size
        messages.forEach {
            adapter.messages.add(MessageInfoData(it,
                blockedUsers.indexOfFirst { user -> user.id == it.user.id } != -1,
                pinnedMessages.indexOfFirst { m -> m.id == it.id } != -1))
            Log.d(FRAGMENT, "Text: ${it.textMessage?.text}, ${it.user.blocked}")
        }
        viewLoading?.visibility = View.GONE
        if (adapter.messages.isEmpty()) {
            textNoHistoryMessage?.visibility = View.VISIBLE
        } else {
            textNoHistoryMessage?.visibility = View.GONE
        }

        if (chatroomIsMuted.not()) {
            inputTextField?.isEnabled = true
            buttonSend?.isEnabled = true
            buttonUnlike?.isEnabled = true
            buttonLike?.isEnabled = true
            context?.let {
                inputTextField?.setHint(R.string.message_to_send_hint)
            }
        } else {
            context?.let {
                inputTextField?.setHint(R.string.chatroom_is_muted_hint)
            }
        }
        adapter.notifyItemRangeChanged(beforeIndex, adapter.itemCount)
        recyclerView?.scrollToPosition(adapter.itemCount - 1)
    }

    override fun onDeleteMessageSuccess(messageId: String) {
        Log.d(FRAGMENT, "onDeleteMessageSuccess: $messageId")
        val deletedMessageIndex = adapter.messages.indexOfFirst { it.messageInfo.id == messageId }
        if (deletedMessageIndex != -1) {
            adapter.messages.removeAt(deletedMessageIndex)
            adapter.notifyItemChanged(deletedMessageIndex)
        }
        toast("Delete message success")
    }

    override fun onUpdateViewerInfoSuccess() {
        Log.d(FRAGMENT, "onUpdateViewerInfoSuccess")
        updateViewerInfoDialogFragment?.dismiss()
        toast("Viewer info update success")
    }

    override fun onUpdateUserSuccess() {
        Log.d(FRAGMENT, "onUpdateUserSuccess")
        toast("User update success")
    }

    override fun onRefreshTokenSuccess() {
        Log.d(FRAGMENT, "onRefreshTokenSuccess")
        toast("Refresh token success")
    }

    override fun onCustomCountersInit(customCounters: List<CustomCounter>) {
        Log.d(FRAGMENT, "onCustomMessageCountsInit $customCounters")
        for (customCounter in customCounters) {
            if (customCounter.key == CUSTOM_MESSAGE_KEY_LIKE) {
                tvLikeCounter?.text = customCounter.value.toString()
            }
        }
        toast("Custom Message Counts Init")
    }

    override fun onCustomCountersUpdated(customCounters: List<CustomCounter>) {
        Log.d(FRAGMENT, "onCustomCountersUpdated $customCounters")
        for(customCounter in customCounters) {
            if (customCounter.key == CUSTOM_MESSAGE_KEY_LIKE) {
                tvLikeCounter?.text = customCounter.value.toString()
            }
        }
        toast("Custom Counters Updated")
    }

    override fun onCustomMessageCountUpdated(increment: Int, customCounters: CustomCounter) {
        Log.d(FRAGMENT, "onCustomMessageCountUpdated $increment $customCounters")
        if (customCounters.key == CUSTOM_MESSAGE_KEY_LIKE) {
            tvLikeCounter?.text = customCounters.value.toString()
        }
        toast("Custom Message Count Updated")
    }

    override fun onError(exception: MessageException) {
        Log.d(FRAGMENT, "onError $exception")
        errorMessageDialogFragment?.dismiss()

        when(exception.reason) {
            MessageException.ERROR_REASON_CREATE_CHATROOM_TOKEN.second-> {
                errorMessageDialogFragment = ErrorMessageDialogFragment(exception.code.toString(), exception.errorMessage, exception.reason, null) {
                    chatroomUser.isConnected = false
                    (activity as MainActivity?)?.replaceFragments(this, StartChatroomFragment())
                }
            }
            MessageException.ERROR_REASON_TOKEN_EXPIRED.second -> {
                errorMessageDialogFragment = ErrorMessageDialogFragment(exception.code.toString(), exception.errorMessage, exception.reason, {
                    Log.d(FRAGMENT, "Reconnect chatroom")
                    presenter.connectChatroom()
                    errorMessageDialogFragment?.dismiss()
                }, {
                    Log.d(FRAGMENT, "Reconnect chatroom")
                    presenter.connectChatroom()
                })
            }
            else -> {
                errorMessageDialogFragment = ErrorMessageDialogFragment(exception.code.toString(), exception.errorMessage, exception.reason)
            }
        }
        errorMessageDialogFragment?.show(parentFragmentManager, "Error Message Dialog")
    }

    /* MessageActionListener */
    override fun onDeleteMessageClicked(
        messageID: String,
        userCustomName: String,
        timestampReceivedAt: String?
    ) {
        Log.d(FRAGMENT, "message view holder onDeleteMessageClicked: $messageID")
        presenter.deleteMessage(messageID, userCustomName, timestampReceivedAt)
    }

    override fun onPinMessageClicked(
        messageID: String, text: String, userID: String, userDeviceID: String, userCustomName: String
    ) {
        Log.d(FRAGMENT, "message view holder onPinMessageClicked: $messageID")
        presenter.pinMessage(messageID, text, userID, userDeviceID, userCustomName)
    }

    override fun onUnpinMessageClicked(messageID: String) {
        Log.d(FRAGMENT, "message view holder onUnpinMessageClicked: $messageID")
        presenter.unpinMessage(messageID)
    }

    override fun onBlockUserClicked(userID: String, userDeviceID: String, userCustomName: String) {
        Log.d(FRAGMENT, "message view holder onBlockUserClicked: $userID")
        presenter.blockUser(userID, userDeviceID, userCustomName)
    }

    override fun onUnblockUserClicked(userID: String) {
        Log.d(FRAGMENT, "message view holder onUnblockUserClicked: $userID")
        presenter.unblockUser(userID)
    }

    /* ChatroomInfoActionListener */
    override fun onUpdateViewerInfoClicked(enabled: Boolean) {
        Log.d(FRAGMENT, "dialog onUpdateViewerInfoClicked")
        presenter.updateViewerInfo(enabled, chatroomUser.username)
        updateViewerInfoDialogFragment?.dismiss()
    }

    /* PinnedMessageDialogActionListener */
    override fun onDialogUnpinMessageClicked(messageID: String) {
        Log.d(FRAGMENT, "dialog onUpdateViewerInfoClicked")
        presenter.unpinMessage(messageID)
    }

    /* BlockedUserDialogActionListener */
    override fun onDialogUnblockUserClicked(userID: String) {
        Log.d(FRAGMENT, "dialog onUnblockUserClicked")
        presenter.unblockUser(userID)
    }

    private fun toast(text: String, time: Int = Toast.LENGTH_LONG) {
        context?.let {
            Toast.makeText(it, text, time).show()
        }
    }
}
