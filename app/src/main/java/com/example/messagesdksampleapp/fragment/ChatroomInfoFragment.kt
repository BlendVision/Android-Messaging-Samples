package com.example.messagesdksampleapp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.messagesdksampleapp.BuildConfig
import com.example.messagesdksampleapp.ChatroomInfo
import com.example.messagesdksampleapp.activity.MainActivity
import com.example.messagesdksampleapp.R
import com.example.messagesdksampleapp.adapter.MessageInfoAdapter

private const val FRAGMENT: String = "MessageInfoFragment"
class ChatroomInfoFragment(private val chatroomFragment: ChatroomFragment, private val data: MutableList<ChatroomInfo> = mutableListOf()) : Fragment() {
    private var toolbar: Toolbar? = null
    private var recyclerView: RecyclerView? = null
    private var versionTextView: TextView? = null
    private var buttonBack: Button? = null
    private val adapter = MessageInfoAdapter()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chatroom_info, container, false)
        recyclerView = view?.findViewById(R.id.recycler_info)
        buttonBack = view?.findViewById(R.id.button_back)
        toolbar = view?.findViewById(R.id.toolbar)
        versionTextView = view?.findViewById(R.id.view_version)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.data = data
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        buttonBack?.setOnClickListener { (activity as MainActivity?)?.backToChatroom(this, chatroomFragment) }
        versionTextView?.text = getString(R.string.message_sdk_version, BuildConfig.MESSAGE_SDK_VERSION)
    }
}