package com.example.findmeapp.view.main.fragment.chat

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.findmeapp.R
import com.example.findmeapp.databinding.ImageMessageBinding
import com.example.findmeapp.databinding.ItemHomeBinding
import com.example.findmeapp.databinding.ItemMessageBinding
import com.example.findmeapp.model.Post
import com.example.findmeapp.view.main.fragment.chat.MessageActivity.Companion.ANONYMOUS
import com.example.findmeapp.view.main.fragment.chat.data.FriendlyMessage
import com.example.findmeapp.view.main.fragment.profile.adapter.AdapterFragmentProfile
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class AdapterMessage(
    private val list: List<FriendlyMessage>,
    private val currentUserName: String?
) : RecyclerView.Adapter<AdapterMessage.ItemViewHolder>() {


    inner class ItemViewHolder(private val binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: FriendlyMessage) {
            Log.d("TAGMo7ista", "bind: $item")
            binding.messageTextView.text = item.textMessage
            setTextColor(item.uidSender, binding.messageTextView)
            binding.messengerTextView.text = item.nameSender
            Glide.with(binding.root.context).load(item.photoUser).into(binding.messengerImageView)
        }
    }

    private fun setTextColor(uidSender: String?, textView: TextView) {
        Log.d("aaa", "currentUserName => $currentUserName /  uidSender => $uidSender")
        if (currentUserName == uidSender) {
            textView.setBackgroundResource(R.drawable.rounded_message_blue)
            textView.setTextColor(Color.WHITE)
        } else {
            textView.setBackgroundResource(R.drawable.rounded_message_gray)
            textView.setTextColor(Color.BLACK)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_message, parent, false)
        val binding = ItemMessageBinding.bind(view)
        Log.d("TAGMo7ista", "onCreateViewHolderInAdapterProfile: ")
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        Log.d("TAGMo7ista", "onBindViewHolder: ")
        holder.bind(list[position])
    }
}