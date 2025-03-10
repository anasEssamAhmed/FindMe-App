package com.example.findmeapp.view.main.fragment.chat

import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findmeapp.R
import com.example.findmeapp.databinding.ActivityMessageBinding
import com.example.findmeapp.model.Repository
import com.example.findmeapp.model.UserItemChat
import com.example.findmeapp.model.UserList
import com.example.findmeapp.view.main.fragment.chat.data.FriendlyMessage
import com.example.findmeapp.viewModel.MainViewModel
import com.example.findmeapp.viewModel.ViewModelFactory
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MessageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMessageBinding
    private var repository: Repository = Repository(FirebaseAuth.getInstance())
    private val TAG = "TAGMo7ista"
    private lateinit var senderId: String
    private lateinit var receiverId: String
    private lateinit var senderName: String
    private lateinit var photoUser: String
    private lateinit var textMessage: String
    private lateinit var manager: LinearLayoutManager
    val viewModel: MainViewModel by viewModels {
        ViewModelFactory(repository)
    }
    private lateinit var adapter: AdapterMessage
    private lateinit var messagesRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_message)



        binding.messageActivity = this
        binding.lifecycleOwner = this
        val chatId = intent.getStringExtra("ChatId").toString()
        val uidChat: UserList = intent.getParcelableExtra<UserList>("uidChat")!!

        if (uidChat.chatId.isNotEmpty()){
            senderId = uidChat.chatId.split("-")[0]
            receiverId = uidChat.chatId.split("-")[1]
            Log.d(TAG, "onCreateMessageActivity: -> $uidChat")
        } else if (chatId.isNotEmpty()){
            senderId = chatId.split("-")[0]
            receiverId = chatId.split("-")[1]
        } else {
            Log.e(TAG, "onCreateMessageActivity: -> Error")
        }




        binding.viewModel = viewModel
        viewModel.getUserInfo()
        messagesRef = Firebase.database.getReference("message")
        viewModel.statusMessage.observe(this) { message ->
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.addNewChatViewModel(UserItemChat(senderId, receiverId, chatId))

        Log.d(TAG, "senderId: $senderId => receiverId: $receiverId")
        viewModel.getInformation()
        viewModel.user.observe(this) {
            if (it != null) {
                senderId = it.id!!
                senderName = it.name
                photoUser = it.imgUrl
            }
        }
        // كل الامور سليمة , هين راح تستقبل البيانات بشكل صحيح
        viewModel.getAllChatViewModel(chatId)
        viewModel.messages.observe(this) {

            // The FirebaseRecyclerAdapter class and options come from the FirebaseUI library
            val options = FirebaseRecyclerOptions.Builder<FriendlyMessage>()
                .setQuery(messagesRef, FriendlyMessage::class.java)
                .build()
            adapter = AdapterMessage(it, senderId)
            binding.progressBar.visibility = ProgressBar.INVISIBLE
            manager = LinearLayoutManager(this)
            manager.stackFromEnd = true
            binding.messageRecyclerView.layoutManager = manager
            binding.messageRecyclerView.adapter = adapter
            Log.d("aaa", "in Message Activity: $it")
        }

        binding.messageEditText.addTextChangedListener(MyButtonObserver(binding.sendButton))
        binding.sendButton.setOnClickListener {
            textMessage = binding.messageEditText.text.toString()
            val friendlyMessage = FriendlyMessage(
                textMessage = textMessage,
                nameSender = senderName,
                photoUser = photoUser,
                uidSender = senderId,
                uidReceiver = receiverId
            )
            viewModel.sendMessageViewModel(chatId, friendlyMessage)
        }
        viewModel.ifSendMessage.observe(this) {
            if (it) {
                binding.messageEditText.text.clear()
            }
        }
    }

    companion object {
        const val TAG = "MainActivityTAG"
        const val MESSAGES_CHILD = "messages"
        const val ANONYMOUS = "anonymous"
        private const val LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif"
    }
}