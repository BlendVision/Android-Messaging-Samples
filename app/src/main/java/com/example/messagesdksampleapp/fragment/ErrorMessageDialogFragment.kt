package com.example.messagesdksampleapp.fragment

import android.content.DialogInterface
import android.content.DialogInterface.OnCancelListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.messagesdksampleapp.R

class ErrorMessageDialogFragment(private val errorCode: String, private val errorMessage: String, private val errorReason: String, private val onReconnectListener: OnClickListener? = null, private val onCancelListener: OnCancelListener? = null) : DialogFragment() {
    private var textErrorCode: TextView? = null
    private var textErrorMessage: TextView? = null
    private var textErrorReason: TextView? = null
    private var buttonReconnect: Button? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_error_dialog, container)
        textErrorCode = view.findViewById(R.id.text_error_code_detail)
        textErrorMessage = view.findViewById(R.id.text_error_message_detail)
        textErrorReason = view.findViewById(R.id.text_error_reason_detail)
        buttonReconnect = view.findViewById(R.id.button_reconnect)
        dialog?.window?.setBackgroundDrawable(ContextCompat.getDrawable(view.context, R.drawable.bg_dialog))

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textErrorCode?.text = errorCode
        textErrorMessage?.text = errorMessage
        textErrorReason?.text = errorReason
        onReconnectListener?.let {
            buttonReconnect?.visibility = View.VISIBLE
            buttonReconnect?.setOnClickListener(onReconnectListener)
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        onCancelListener?.onCancel(dialog)
    }
}
