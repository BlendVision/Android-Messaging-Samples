package com.example.messagesdksampleapp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.blendvision.chat.messaging.common.presentation.CustomCounter
import com.blendvision.chat.messaging.common.presentation.MessageException
import com.blendvision.chat.messaging.common.presentation.MessageInfo
import com.blendvision.chat.messaging.common.presentation.MessageType
import com.blendvision.chat.messaging.common.presentation.Self
import com.blendvision.chat.messaging.message.presentation.state.ConnectionState
import com.example.messagesdksampleapp.R
import com.example.messagesdksampleapp.activity.MainActivity
import com.example.messagesdksampleapp.databinding.FragmentMessageFilterBinding
import com.example.messagesdksampleapp.listener.ChatroomPresenterListener
import com.example.messagesdksampleapp.listener.MessageFilterActionListener
import com.example.messagesdksampleapp.presenter.ChatroomPresenter
import com.google.gson.Gson
import com.google.gson.GsonBuilder


class MessageFilterFragment(
    private val presenter: ChatroomPresenter,
    private val chatroomFragment: ChatroomFragment
) : Fragment(), ChatroomPresenterListener, MessageFilterActionListener {

    private var selectedTypes: MutableSet<MessageType> = mutableSetOf()

    private val binding: FragmentMessageFilterBinding by lazy {
        FragmentMessageFilterBinding.inflate(layoutInflater)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.initView()
        return binding.root
    }

    private fun FragmentMessageFilterBinding.initView() {
        val fragment = this@MessageFilterFragment
        presenter.bind(fragment)
        buttonBack.setOnClickListener {
            presenter.bind(chatroomFragment)
            (activity as MainActivity?)?.backToChatroom(
                fragment,
                chatroomFragment
            )
        }
        messageTypeLayout.setOnClickListener {
            (activity as MainActivity?)?.replaceFragments(
                fragment, MessageFilterTypeFragment(
                    fragment = fragment,
                    listener = fragment,
                    selectedTypes = selectedTypes
                ), true
            )
        }
    }

    override fun onChatroomConnectionChanged(connectionState: ConnectionState) {
        Log.d(TAG, connectionState.toString())
    }

    override fun onGetChatHistorySuccess(messages: List<MessageInfo>) {
        Log.d(TAG, "onGetChatHistorySuccess: ${Gson().toJson(messages)}")
    }

    override fun onDeleteMessageSuccess(messageId: String) {
        Log.d(TAG, "onDeleteMessageSuccess: $messageId")
    }

    override fun onUpdateViewerInfoSuccess() {
        Log.d(TAG, "onUpdateViewerInfoSuccess")
    }

    override fun onUpdateUserSuccess() {
        Log.d(TAG, "onUpdateUserSuccess")
    }

    override fun onRefreshTokenSuccess() {
        Log.d(TAG, "onRefreshTokenSuccess")
    }

    override fun onError(exception: MessageException) {
        Log.d(TAG, "onError $exception")
        toast("Error: ${exception.errorMessage}")
    }

    override fun onReceiveMessage(messages: List<MessageInfo>) {
        Log.d(TAG, "onReceiveMessage: ${Gson().toJson(messages)}")
    }

    override fun onCustomCountersInit(customCounters: List<CustomCounter>) {
        Log.d(TAG, "onCustomMessageCountsInit $customCounters")
    }

    override fun onCustomCountersUpdated(customCounters: List<CustomCounter>) {
        Log.d(TAG, "onCustomCountersUpdated $customCounters")
    }

    override fun onCustomMessageCountUpdated(increment: Int, customCounters: CustomCounter) {
        Log.d(TAG, "onCustomMessageCountUpdated $increment $customCounters")
    }

    override fun onGetSelfInfoSuccess(self: Self) {
        Log.d(TAG, "onGetSelfInfoSuccess $self")
    }

    override fun onGetMessagesSuccess(messages: List<MessageInfo>) {
        Log.d(TAG, "onGetMessagesSuccess ${Gson().toJson(messages)}")
        val gson = GsonBuilder().setPrettyPrinting().create()
        binding.textView.text = gson.toJson(messages)
    }

    override fun onFilterButtonClicked(types: Set<MessageType>?) {
        selectedTypes = types?.toMutableSet() ?: mutableSetOf()
        presenter.stopGettingMessagesPeriodically()
        presenter.startGettingMessagesPeriodically(selectedTypes.map { it.toString() }
            .toTypedArray())
        binding.messageTypes.text = types?.joinToString(", ") ?: getString(R.string.none)
    }

    private fun toast(text: String, time: Int = Toast.LENGTH_LONG) {
        context?.let {
            Toast.makeText(it, text, time).show()
        }
    }

    companion object {
        private const val TAG = "MessageFilterFragment"
    }
}