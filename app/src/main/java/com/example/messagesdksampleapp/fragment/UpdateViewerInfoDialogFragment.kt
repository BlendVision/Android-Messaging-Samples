package com.example.messagesdksampleapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.messagesdksampleapp.listener.ChatroomInfoActionListener
import com.example.messagesdksampleapp.R

class UpdateViewerInfoDialogFragment(private val listener: ChatroomInfoActionListener, private var viewerInfoEnable: Boolean) : DialogFragment() {
    private var switchEnable: SwitchCompat? = null
    private var buttonCancel: Button? = null
    private var buttonUpdate: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_update_viewer_info_dialog, container)
        switchEnable = view.findViewById(R.id.switch_enable)
        buttonUpdate = view.findViewById(R.id.button_update)
        buttonCancel = view.findViewById(R.id.button_cancel)
        switchEnable?.isChecked = viewerInfoEnable
        dialog?.window?.setBackgroundDrawable(ContextCompat.getDrawable(view.context, R.drawable.bg_dialog))

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonUpdate?.setOnClickListener {
            listener.onUpdateViewerInfoClicked(switchEnable?.isChecked == true)
        }

        buttonCancel?.setOnClickListener {
            dismiss()
        }
    }
}
