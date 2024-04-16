package com.example.messagesdksampleapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.SwitchCompat
import com.example.messagesdksampleapp.activity.MainActivity
import com.example.messagesdksampleapp.R
import com.google.android.material.textfield.TextInputEditText
import java.util.UUID

private const val FRAGMENT: String = "StartChatroomFragment"
class StartChatroomFragment : Fragment() {
    private var textApiToken: TextInputEditText? = null
    private var textOrgID: TextInputEditText? = null
    private var textChatroomID: TextInputEditText? = null
    private var textUserName: TextInputEditText? = null
    private var textDeviceID: TextInputEditText? = null
    private var switchIsAdmin: SwitchCompat? = null
    private var buttonConnect: Button? = null
    private var buttonGenerate: Button? = null

    private val connectButtonOnClickListener = OnClickListener { _ ->
        Log.d(FRAGMENT,"buttonConnect click")
        (activity as MainActivity?)?.let {
            when(it.bottomNavigationSelectedItemId()) {
                it.bottomNavigationMenuItemId(0) -> {
                    it.tabChatroomUser1.username = textUserName?.text.toString()
                    it.tabChatroomUser1.isAdmin = switchIsAdmin?.isChecked == true
                    it.tabChatroomUser1.isConnected = true
                    it.tabChatroomUser1.deviceID = textDeviceID?.text.toString()
                    it.tabChatroomUser1.clientID = textDeviceID?.text.toString()
                    it.tabChatroomUser1.isBlock = false
                    Log.d(FRAGMENT,"userID: ${textDeviceID?.text}")
                    it.replaceFragments(this, ChatroomFragment(it.tabChatroomUser1, textDeviceID?.text.toString(), textChatroomID?.text.toString(), textApiToken?.text.toString(), textOrgID?.text.toString()))
                }
                it.bottomNavigationMenuItemId(1) -> {
                    it.tabChatroomUser2.username = textUserName?.text.toString()
                    it.tabChatroomUser2.isAdmin = switchIsAdmin?.isChecked == true
                    it.tabChatroomUser2.isConnected = true
                    it.tabChatroomUser2.deviceID = textDeviceID?.text.toString()
                    it.tabChatroomUser2.clientID = textDeviceID?.text.toString()
                    it.tabChatroomUser2.isBlock = false
                    it.replaceFragments(this, ChatroomFragment(it.tabChatroomUser2, textDeviceID?.text.toString(), textChatroomID?.text.toString(), textApiToken?.text.toString(), textOrgID?.text.toString()))
                }
                it.bottomNavigationMenuItemId(2) -> {
                    it.tabChatroomUser3.username = textUserName?.text.toString()
                    it.tabChatroomUser3.isAdmin = switchIsAdmin?.isChecked == true
                    it.tabChatroomUser3.isConnected = true
                    it.tabChatroomUser3.deviceID = textDeviceID?.text.toString()
                    it.tabChatroomUser3.clientID = textDeviceID?.text.toString()
                    it.tabChatroomUser3.isBlock = false
                    it.replaceFragments(this, ChatroomFragment(it.tabChatroomUser3, textDeviceID?.text.toString(), textChatroomID?.text.toString(), textApiToken?.text.toString(), textOrgID?.text.toString()))
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_start_chatroom, container, false)
        textApiToken = view?.findViewById(R.id.text_api_token)
        textOrgID = view?.findViewById(R.id.text_org_id)
        textChatroomID = view?.findViewById(R.id.text_chatroom_id)
        textUserName = view?.findViewById(R.id.text_username)
        textDeviceID = view?.findViewById(R.id.text_device_id)
        switchIsAdmin = view?.findViewById(R.id.switch_is_admin)
        buttonConnect = view?.findViewById(R.id.button_connect)
        buttonGenerate = view?.findViewById(R.id.button_generate_device_id)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonConnect?.setOnClickListener(connectButtonOnClickListener)
        buttonGenerate?.setOnClickListener {
            textDeviceID?.setText(UUID.randomUUID().toString())
        }
    }
}