package com.example.messagesdksampleapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.blendvision.chat.messaging.common.presentation.MessageType
import com.example.messagesdksampleapp.activity.MainActivity
import com.example.messagesdksampleapp.adapter.MessageFilterTypeAdapter
import com.example.messagesdksampleapp.databinding.FragmentMessageFilterTypeDialogBinding
import com.example.messagesdksampleapp.listener.MessageFilterActionListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MessageFilterTypeFragment(
    private val fragment: MessageFilterFragment,
    private val listener: MessageFilterActionListener
) : Fragment() {
    private val binding: FragmentMessageFilterTypeDialogBinding by lazy {
        FragmentMessageFilterTypeDialogBinding.inflate(layoutInflater)
    }
    private val viewModel: MessageFilterViewModel by activityViewModels()
    private lateinit var adapter: MessageFilterTypeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.initView()
        return binding.root
    }

    private fun FragmentMessageFilterTypeDialogBinding.initView() {
        val messageTypes: List<MessageType> =
            MessageType::class.sealedSubclasses.mapNotNull { it.objectInstance }

        adapter = MessageFilterTypeAdapter(messageTypes, viewModel.selectedTypes.value.toMutableSet())
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        lifecycleScope.launch {
            viewModel.selectedTypes.collect { selectedTypes ->
                adapter.updateSelectedTypes(selectedTypes)
            }
        }

        buttonBack.setOnClickListener {
            viewModel.updateSelectedTypes(adapter.getSelectedTypes())
            (activity as MainActivity?)?.backToChatroom(
                this@MessageFilterTypeFragment,
                fragment
            )
        }
    }

    companion object {
        private const val TAG = "MessageFilterTypeDialogFragment"
    }
}