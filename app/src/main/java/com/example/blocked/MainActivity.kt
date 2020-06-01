package com.example.blocked

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val LAUNCH_SECOND_ACTIVITY = 1

    private val newsViewModel: ContactsViewModel by lazy {
        ViewModelProviders.of(this).get(ContactsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decor = window.decorView
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        floatingActionButton.setOnClickListener {
            startActivityForResult(Intent(this,
                AddContactActivity::class.java),LAUNCH_SECOND_ACTIVITY)
        }

        newsViewModel.getContacts()
        newsViewModel.contactList.observe(this, Observer {
            it.forEach { Log.i("Numbers" , it.number)}
            //IncomingCallReceiver().numbersList = it
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LAUNCH_SECOND_ACTIVITY && resultCode==Activity.RESULT_OK){
            newsViewModel.insertContact("+91${data?.getStringExtra("result")}")

        }
    }


}
