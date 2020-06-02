package com.example.blocked.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.blocked.R
import kotlinx.android.synthetic.main.activity_add_contact.*


class AddContactActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)
        root.setOnClickListener {
            closeDialog()
        }
        saveButton.setOnClickListener {
            closeDialog()
        }
    }

    private fun closeDialog() {
        val returnIntent = Intent()
        if (editText.text.isNotEmpty()) {
            returnIntent.putExtra("result", editText.text.toString())
            setResult(Activity.RESULT_OK, returnIntent)
        } else {
            setResult(Activity.RESULT_CANCELED, returnIntent);
        }
        window.decorView.animate().alpha(0f).withEndAction { finish() }.start()
    }
}
