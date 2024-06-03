package com.example.messagesdksampleapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import com.example.messagesdksampleapp.activity.MainActivity
import com.example.messagesdksampleapp.R
import com.google.android.material.textfield.TextInputEditText

private const val FRAGMENT: String = "StartChatroomFragment"
class StartChatroomFragment : Fragment() {
    private var textChatRoomToken: TextInputEditText? = null
    private var textRefreshToken: TextInputEditText? = null
    private var textUpdateInterval: TextInputEditText? = null
    private var textBatchInterval: TextInputEditText? = null
    private var buttonConnect: Button? = null

    private val connectButtonOnClickListener = OnClickListener { _ ->
        Log.d(FRAGMENT, "buttonConnect click")
        (activity as MainActivity?)?.let {

            val updateInterval: Long? = if (textUpdateInterval?.text.toString().isNotEmpty()) {
                textUpdateInterval?.text.toString().toLong()
            } else {
                null
            }
            val batchInterval: Long? = if (textBatchInterval?.text.toString().isNotEmpty()) {
                textBatchInterval?.text.toString().toLong()
            } else {
                null
            }

            when (it.bottomNavigationSelectedItemId()) {
                it.bottomNavigationMenuItemId(0) -> {
                    it.replaceFragments(
                        this,
                        ChatroomFragment(
                            textChatRoomToken?.text.toString(),
                            textRefreshToken?.text.toString(),
                            updateInterval,
                            batchInterval
                        )
                    )
                }

                it.bottomNavigationMenuItemId(1) -> {
                    it.replaceFragments(
                        this,
                        ChatroomFragment(
                            textChatRoomToken?.text.toString(),
                            textRefreshToken?.text.toString(),
                            updateInterval,
                            batchInterval
                        )
                    )
                }

                it.bottomNavigationMenuItemId(2) -> {
                    it.replaceFragments(
                        this,
                        ChatroomFragment(
                            textChatRoomToken?.text.toString(),
                            textRefreshToken?.text.toString(),
                            updateInterval,
                            batchInterval
                        )
                    )
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_start_chatroom, container, false)
        textChatRoomToken = view?.findViewById(R.id.text_chat_room_token)
        textRefreshToken = view?.findViewById(R.id.text_refresh_token)
        textUpdateInterval = view?.findViewById(R.id.text_update_interval)
        textBatchInterval = view?.findViewById(R.id.text_batch_interval)
        buttonConnect = view?.findViewById(R.id.button_connect)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonConnect?.setOnClickListener(connectButtonOnClickListener)
    }
}