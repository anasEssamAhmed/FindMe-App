package com.example.findmeapp.view.main.fragment.chat

import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import com.example.findmeapp.R

class MyButtonObserver(private val button: ImageView) : TextWatcher {
    override fun onTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {
        if (charSequence.toString().trim().isNotEmpty()) {
            button.isEnabled = true
            button.setImageResource(R.drawable.outline_send_24)
        } else {
            button.isEnabled = false
            button.setImageResource(R.drawable.outline_send_gray_24)
        }
    }

    override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun afterTextChanged(editable: Editable) {}
}