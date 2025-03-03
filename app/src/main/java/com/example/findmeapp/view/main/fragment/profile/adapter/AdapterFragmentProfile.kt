package com.example.findmeapp.view.main.fragment.profile.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.findmeapp.R
import com.example.findmeapp.databinding.ItemHomeBinding
import com.example.findmeapp.model.Post
import com.example.findmeapp.view.main.fragment.profile.OnClickItemPostToEdit

class AdapterFragmentProfile(
    private var listener: OnClickItemPostToEdit,
    private val listenerEdit: OnEditButtonClickListener,
    private val posts: ArrayList<Post>
) : RecyclerView.Adapter<AdapterFragmentProfile.ItemViewHolder>() {

    interface OnEditButtonClickListener {
        fun onEditButtonClick(postId: String)
    }

    inner class ItemViewHolder(private val binding: ItemHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: Post) {
            Log.d("TAGMo7ista", "bind: $item")
            binding.tvNameMissing.text = item.nameMissing
            binding.tvAgeMissing.text = item.ageMissing
            binding.tvDescription.text = item.description
            Glide.with(binding.root.context).load(item.missingPersonImage).into(binding.shapeableImageView)
            binding.btnContact.text = "تعديل"
            binding.btnContact.setOnClickListener {
                listener.onClickPostToEdit(item)
                listenerEdit.onEditButtonClick(item.idPost)
                Log.d("TAGMo7ista", "bind: OnClick")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_home, parent, false)
        val binding = ItemHomeBinding.bind(view)
        Log.d("TAGMo7ista", "onCreateViewHolderInAdapterProfile: ")
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        Log.d("TAGMo7ista", "onBindViewHolder: ")
        holder.bind(posts[position])
    }
}