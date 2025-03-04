package com.example.findmeapp.view.main.fragment.chat

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.findmeapp.R
import com.example.findmeapp.databinding.ActivityMessageBinding
import com.example.findmeapp.model.Repository
import com.example.findmeapp.view.main.fragment.chat.data.FriendlyMessage
import com.example.findmeapp.viewModel.MainViewModel
import com.example.findmeapp.viewModel.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class MessageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMessageBinding
    private var repository: Repository = Repository(FirebaseAuth.getInstance())
    private val TAG = "TAGMo7ista"
    private lateinit var senderId: String
    private lateinit var receiverId: String
    private lateinit var senderName: String
    private lateinit var photoUser: String
    private lateinit var textMessage: String
    val viewModel: MainViewModel by viewModels {
        ViewModelFactory(repository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_message)


        binding.messageActivity = this
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        viewModel.getUserInfo()
        viewModel.statusMessage.observe(this) { message ->
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }

        val chatId = intent.getStringExtra("ChatId").toString()
        senderId = chatId.split("-")[0]
        receiverId = chatId.split("-")[1]
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
        viewModel.messages.observe(this){
            Log.d("aaa" , "in Message Activity: $it")
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
}