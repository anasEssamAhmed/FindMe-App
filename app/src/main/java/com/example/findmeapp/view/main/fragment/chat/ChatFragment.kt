package com.example.findmeapp.view.main.fragment.chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.findmeapp.databinding.FragmentChatBinding
import com.example.findmeapp.model.Repository
import com.example.findmeapp.model.User
import com.example.findmeapp.model.UserList
import com.example.findmeapp.viewModel.MainViewModel
import com.example.findmeapp.viewModel.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch


class ChatFragment : Fragment(), OnClickItemMessage, OnClickItemChat {

    private lateinit var binding: FragmentChatBinding
    private val database = Firebase.database
    private val auth = FirebaseAuth.getInstance()
    private var repository: Repository = Repository(FirebaseAuth.getInstance())
    private val allUserList = ArrayList<UserList>()
    private lateinit var adapter: AdapterRecyclerItem
    private lateinit var listenerItemChat: OnClickItemChat
    val viewModel: MainViewModel by viewModels {
        ViewModelFactory(repository)
    }

    //    private lateinit var adapter: AdapterRecyclerItem
    private lateinit var listener: OnClickItemMessage

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        listener = this
        listenerItemChat = this
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.topAppBar.setNavigationOnClickListener {
            binding.appBarLayout.visibility = View.GONE
            binding.searchView.isFocusable = true
            binding.searchView.isIconified = false
        }
        binding.searchView.setOnCloseListener {
            binding.appBarLayout.visibility = View.VISIBLE
            false
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Add your code here to handle the query submission
                Log.d("onQueryTextChangeTAG", "text: $query")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Add your code here to handle the query text changing
                Log.d("onQueryTextChangeTAG", "text: $newText")
                return true
            }
        })
        lifecycleScope.launch {
            viewModel.getMyChatListViewModel(auth.uid!!)

            viewModel.isGetData.observe(viewLifecycleOwner) { message ->
                if (message) {
                    val arrayListChatUserList = viewModel.userList
                    allUserList.addAll(arrayListChatUserList)
                    Log.d("TAGMo7ista", "onViewCreated: -> $allUserList")
                    adapter = AdapterRecyclerItem(allUserList, listenerItemChat)
                    binding.progressBar.visibility = ProgressBar.INVISIBLE
                    binding.recyclerViewItem.adapter = adapter
                }
            }
        }
    }

    override fun onClick(uid: User) {
        startActivity(
            Intent(requireContext(), MessageActivity::class.java).putExtra(
                "uidSender",
                uid.id
            )
        )
    }

    override fun onClick(uidChat: UserList) {
        Log.d("TAGMo7ista", "onClick: uidChat-> $uidChat")
        startActivity(
            Intent(requireContext(), MessageActivity::class.java).putExtra(
                "uidChat",
                uidChat
            )
        )
    }

}