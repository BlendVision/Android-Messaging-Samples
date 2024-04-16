package com.example.messagesdksampleapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blendvision.chat.messaging.common.presentation.BlockedUser
import com.example.messagesdksampleapp.R
import com.example.messagesdksampleapp.adapter.BlockedUserAdapter
import com.example.messagesdksampleapp.listener.BlockedUserDialogActionListener

class BlockedUserDialogFragment(listener: BlockedUserDialogActionListener, private val blockedUser: MutableList<BlockedUser>) : DialogFragment() {
    private var recyclerView: RecyclerView? = null
    private var textNoBlockedUser: TextView? = null
    private var adapter = BlockedUserAdapter(listener)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_blocked_user_dialog, container)
        recyclerView = view.findViewById(R.id.recycler_blocked_user)
        textNoBlockedUser = view.findViewById(R.id.text_no_blocked_user)
        dialog?.window?.setBackgroundDrawable(ContextCompat.getDrawable(view.context, R.drawable.bg_dialog))

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter.blockedUser = blockedUser
        adapter.notifyItemRangeChanged(0, blockedUser.size)
    }

    fun refreshBlockedUser(position: Int) {
        adapter.notifyItemRemoved(position)
        if (blockedUser.isEmpty()) {
            textNoBlockedUser?.visibility = View.VISIBLE
        }
    }
}
