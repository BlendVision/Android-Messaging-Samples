package com.example.messagesdksampleapp.fragment

import androidx.lifecycle.ViewModel
import com.blendvision.chat.messaging.common.presentation.MessageType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MessageFilterViewModel : ViewModel() {
    private val _selectedTypes = MutableStateFlow<Set<MessageType>>(emptySet())
    val selectedTypes: StateFlow<Set<MessageType>> = _selectedTypes

    fun updateSelectedTypes(newSelectedTypes: Set<MessageType>) {
        _selectedTypes.value = newSelectedTypes
    }
}