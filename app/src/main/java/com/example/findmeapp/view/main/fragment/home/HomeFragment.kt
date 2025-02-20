package com.example.findmeapp.view.main.fragment.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.findmeapp.databinding.FragmentHomeBinding
import com.example.findmeapp.model.Post
import com.example.findmeapp.model.Repository
import com.example.findmeapp.view.main.AddPostActivity
import com.example.findmeapp.view.main.NotificationActivity
import com.example.findmeapp.view.main.fragment.home.adapter.AdapterFragmentHome
import com.example.findmeapp.viewModel.MainViewModel
import com.example.findmeapp.viewModel.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment(), OnClickItemPost {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var repository: Repository
    private lateinit var posts: ArrayList<Post>
    private lateinit var adapterFragmentHome: AdapterFragmentHome
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var listenerItem: OnClickItemPost
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        repository = Repository(FirebaseAuth.getInstance())
        val viewModel: MainViewModel by viewModels {
            ViewModelFactory(repository)
        }

        binding.fragmentHome = this
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        listenerItem = this
        posts = ArrayList()

        viewModel.getUserInfo()
        viewModel.getAllPost()

        viewModel.isGetData.observe(viewLifecycleOwner) { message ->
            if (message) {
                posts = viewModel.posts
                Log.d("TAGMo7ista", "HomeFragment: $posts")
                adapterFragmentHome = AdapterFragmentHome(auth.currentUser!!.uid,listenerItem, posts)
                viewManager = LinearLayoutManager(requireContext())
                binding.recycleViwHome.apply {
                    setHasFixedSize(true)
                    adapter = adapterFragmentHome
                    layoutManager = viewManager
                }
                Log.d("TAGMo7ista", "onCreateView: After The Recycler")
            }
        }

        binding.fabNotificationHome.setOnClickListener {
            startActivity(Intent(context, NotificationActivity::class.java))
        }

        binding.fabAddPost.setOnClickListener {
            startActivity(Intent(context, AddPostActivity::class.java))
        }

        return binding.root
    }

    override fun onClickPostToContact(post: Post) {
        Log.d("TAGMo7ista", "onClickPostToContactHomeFragment: $post")
    }


}