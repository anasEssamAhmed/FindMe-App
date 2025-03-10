package com.example.findmeapp.view.main.fragment.chat

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.findmeapp.R
import com.example.findmeapp.databinding.ItemChatBinding
import com.example.findmeapp.model.UserList
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class AdapterRecyclerItem(
    private val array: ArrayList<UserList>,
    private var listener: OnClickItemChat
) : RecyclerView.Adapter<AdapterRecyclerItem.ItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val view = inflater.inflate(R.layout.item_chat, parent, false)
        val binding = ItemChatBinding.bind(view)
        return ItemViewHolder(binding)

    }

    override fun getItemCount(): Int = array.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(array[position])
        holder.itemView.setOnClickListener {
            listener.onClick(array[position])
        }
    }


    inner class ItemViewHolder(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: UserList) {
            // TODO: implement
            binding.userName.text = item.user.name
            loadImageIntoView(binding.profileImage,item.user.imgUrl)

        }
    }


    private fun loadImageIntoView(view: ImageView, url: String) {
        if (url.startsWith("gs://")) {
            val storageReference = Firebase.storage.getReferenceFromUrl(url)
            storageReference.downloadUrl
                .addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    loadWithGlide(view, downloadUrl)
                }
                .addOnFailureListener { e ->
                    Log.e("AdapterRecyclerItemTAG", "loadImageIntoView: $e")
                }
        } else {
            loadWithGlide(view, url)
        }
    }

    private fun loadWithGlide(view: ImageView, url: String) {
        Glide.with(view.context).load(url).into(view)
        var requestBuilder = Glide.with(view.context).load(url)
        requestBuilder = requestBuilder.transform(CircleCrop())
        requestBuilder.into(view)
    }

}