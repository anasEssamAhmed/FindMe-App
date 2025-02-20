package com.example.findmeapp.view.main.fragment.profile

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
import com.example.findmeapp.databinding.FragmentProfileBinding
import com.example.findmeapp.model.Post
import com.example.findmeapp.model.Repository
import com.example.findmeapp.view.login.Login
import com.example.findmeapp.view.main.EditPostActivity
import com.example.findmeapp.view.main.fragment.profile.adapter.AdapterFragmentProfile
import com.example.findmeapp.viewModel.MainViewModel
import com.example.findmeapp.viewModel.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment(), OnClickItemPostToEdit, AdapterFragmentProfile.OnEditButtonClickListener {


    private lateinit var binding: FragmentProfileBinding
    private lateinit var repository: Repository
    private lateinit var posts: ArrayList<Post>
    private lateinit var adapterFragmentHome: AdapterFragmentProfile
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var listenerItem: OnClickItemPostToEdit
    val viewModel: MainViewModel by viewModels {
        ViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        repository = Repository(FirebaseAuth.getInstance())


        viewModel.navigateToLogin.observe(viewLifecycleOwner) {
            if (it) {
                startActivity(Intent(context, Login::class.java))
                activity?.finish()
            }
        }

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.fragmentProfile = this
        listenerItem = this


        viewModel.getInformation()
        viewModel.getMyAllPost()

        viewModel.isGetData.observe(viewLifecycleOwner) { isGetData ->
            if (isGetData) {
                posts = viewModel.posts
                Log.d("TAGMo7ista", "ProfileFragment: $posts")
                adapterFragmentHome = AdapterFragmentProfile(listenerItem, this, posts)
                viewManager = LinearLayoutManager(requireContext())
                binding.recycleViwProfile.apply {
                    setHasFixedSize(true)
                    adapter = adapterFragmentHome
                    layoutManager = viewManager
                }
                Log.d("TAGMo7ista", "onCreateView: After The Recycler")
            }
        }

        binding.btnEditInfo.setOnClickListener {
            startActivity(Intent(context, EditInfoActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            viewModel.signOutViewModel()

        }

        return binding.root
    }

    override fun onClickPostToEdit(post: Post) {
        viewModel.post.value = post
        Log.d("TAGMo7ista", "onClickPostToEdit: ${viewModel.post.value}")
        startActivity(Intent(context, EditPostActivity::class.java))
    }

    override fun onEditButtonClick(postId: String) {
        val intent = Intent(requireContext(), EditPostActivity::class.java)
        intent.putExtra("POST_ID", postId)
        startActivity(intent)
    }

}