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
        textChatRoomToken?.setText(
            //QA
            "eyJhbGciOiJSUzI1NiIsImtpZCI6IjU5NjNmYjMyLTM1MGUtNDI2OC05N2MwLTljMzA3Mzg4N2Y3YyIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJjb20ua2tjb21wYW55Lm1lZGlhLnBsYXRmb3JtLmNoYXQiLCJzdWIiOiIwNjlGMDRGOC0zNzUwLTRDRTUtOUY2Ni0wQ0VCM0M1Qzg2MUQiLCJleHAiOjE3MzU1NTIyMjcsImlhdCI6MTcyOTU2NjMxNywianRpIjoiYWQ1YWFlYWItNDc0MC00YTY3LTkxZGQtOTU3NjE4YTY1MDU1IiwidGVuYW50X2lkIjoiOGNhZTg5NDctYmFiOC00ZWUyLWE3NmUtYzZkOTkzZDhkMDI3IiwidHlwZSI6ImNoYXQiLCJjaGF0X2lkIjoiMTY0ZTY5MTYtYzRhYS00ZDBhLTg2YmItMWMxZmQzMjE3MzRiIiwiZGV2aWNlX2lkIjoiZGV2aWNlX2FuZHJvaWQiLCJyb2xlcyI6WyJhZG1pbiJdLCJwb2xpY2llcyI6W3sidCI6ImJpIiwiciI6Imtrc19wbGF0Zm9ybV9jaGF0X2RldiJ9LHsidCI6InMifV19.otL2mHFe2tgWBy8IonebGMPvSt2u_z_p6M28szIp-Z5bDihr94Dlk-0PWu84qG91FwrJsQRGgJ52WdhCLjudLWbLNpXzmUoX2U-jn-RKqp-7ONjE98oDJWsifSYX-AUgtPDQMQxSy1__Muir-HBUfOHLP9fCEAKD11OuyANuBjU2jO0UBJ3sVypZpbPhHeMOhFv8dIHRXLKGba-fxffIscxwrRt6EIdTNVBaYYx3w8xQQhQW4adERvotqJMgFqB5wFVMJ_92oVDnnaT0Fp_yVZdFPVrSVtqCqwh-Y4JQZoCO1oZ7RO7UCXMvtwj5-0sNIoNB4rMC1cLGev2ywAlXsA"
            // PRDO
        )
        textRefreshToken?.setText(
            //QA
            "eyJhbGciOiJSUzI1NiIsImtpZCI6IjU5NjNmYjMyLTM1MGUtNDI2OC05N2MwLTljMzA3Mzg4N2Y3YyIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJjb20ua2tjb21wYW55Lm1lZGlhLnBsYXRmb3JtLmNoYXQiLCJzdWIiOiIwNjlGMDRGOC0zNzUwLTRDRTUtOUY2Ni0wQ0VCM0M1Qzg2MUQiLCJleHAiOjE3MzIxNTgzMTgsImlhdCI6MTcyOTU2NjMxOCwianRpIjoiN2I3ZDQwZTUtN2M4ZC00N2JjLTljNjEtZDQwOWJjZTVmZWJiIiwidGVuYW50X2lkIjoiOGNhZTg5NDctYmFiOC00ZWUyLWE3NmUtYzZkOTkzZDhkMDI3IiwidHlwZSI6ImNoYXRfcmVmcmVzaCIsImNoYXRfaWQiOiIxNjRlNjkxNi1jNGFhLTRkMGEtODZiYi0xYzFmZDMyMTczNGIiLCJkZXZpY2VfaWQiOiJkZXZpY2VfYW5kcm9pZCIsInJvbGVzIjpbImFkbWluIl0sInBvbGljaWVzIjpbeyJ0IjoiYmkiLCJyIjoia2tzX3BsYXRmb3JtX2NoYXRfZGV2In0seyJ0IjoicyJ9XX0.Sh6yFQgKUd93CPBCJH8o1ZX5ZrRayB-QOHB8138wUrV7RYormnV_CMuGhZ4btvej-X-bto0afr9HbHs-yRhnIAPMkng2ufo_h0QwA9ayasxgdDnwC-Wx61b91VHXN2atf5nsG8umRqdWD2PkyrJOqZazj7rKRReIL3roQUk7_Y1SC8ZpNP_78wYrv3L5VmRYt4qYzYKDE-sRMWFovggHiaYpFVywN7WXMZZWBRi6L7ndLnrNFKgyXUCqkTnMcopnPQRxdeLAxTxzB-o9vio2ZN9oDIvjxQGhg2lxW79Si1J1hht3zqYYz3pUXrdZ9SkTMKKCsHgCqHVG3XLZ-sr1Xw"
            //Prod
        )
        buttonConnect?.setOnClickListener(connectButtonOnClickListener)
    }
}