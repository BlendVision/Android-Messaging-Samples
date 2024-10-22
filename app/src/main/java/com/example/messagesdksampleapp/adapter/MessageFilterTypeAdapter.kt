package com.example.messagesdksampleapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blendvision.chat.messaging.common.presentation.MessageType
import com.example.messagesdksampleapp.databinding.ItemMessageTypeBinding

class MessageFilterTypeAdapter(
    private val messageTypes: List<MessageType>,
    private var selectedTypes: MutableSet<MessageType>
) : RecyclerView.Adapter<MessageFilterTypeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMessageTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val messageType = messageTypes[position]
        holder.bind(messageType)
    }

    override fun getItemCount(): Int = messageTypes.size

    inner class ViewHolder(private val binding: ItemMessageTypeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(messageType: MessageType) {
            binding.checkbox.text = messageType.toString()
            binding.checkbox.isChecked = selectedTypes.contains(messageType)

            binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedTypes.add(messageType)
                } else {
                    selectedTypes.remove(messageType)
                }
            }
        }
    }

    fun updateSelectedTypes(newSelectedTypes: Set<MessageType>) {
        selectedTypes = newSelectedTypes.toMutableSet()
        notifyDataSetChanged()
    }

    fun getSelectedTypes(): Set<MessageType> = selectedTypes
}