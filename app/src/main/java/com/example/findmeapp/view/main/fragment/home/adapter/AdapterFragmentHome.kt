package com.example.findmeapp.view.main.fragment.home.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.findmeapp.R
import com.example.findmeapp.databinding.ItemHomeBinding
import com.example.findmeapp.model.Post

class AdapterFragmentHome(
//    context: Context,
    private val idUser:String,
    private var listener: OnContactListener,
    private val posts: ArrayList<Post>
) : RecyclerView.Adapter<AdapterFragmentHome.ItemViewHolder>() {

    interface OnContactListener {
        fun onContactClick(chatId: String)
    }

    inner class ItemViewHolder(private val binding: ItemHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Post) {
            Log.d("TAGMo7ista", "bind: $item")
            binding.tvNameMissing.text = item.nameMissing
            binding.tvAgeMissing.text = item.ageMissing
            binding.tvDescription.text = item.description
//            Glide.with(context).load(item.missingPersonName).into(binding.shapeableImageView)
            if (idUser == item.id){
                binding.btnContact.visibility = View.GONE
            } else {
                binding.btnContact.setOnClickListener {
                    listener.onContactClick(item.id)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_home, parent, false)
        val binding = ItemHomeBinding.bind(view)
        Log.d("TAGMo7ista", "onCreateViewHolder: ")
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        Log.d("TAGMo7ista", "onBindViewHolder: ")
        holder.bind(posts[position])
    }
}