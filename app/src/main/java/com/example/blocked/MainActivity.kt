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

    private val contactsViewModel: ContactsViewModel by lazy {
        ViewModelProviders.of(this).get(ContactsViewModel::class.java)
    }

    private lateinit var adapter:ContactsRecyclerAdapter

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

        adapter = ContactsRecyclerAdapter {
            Log.i("Delete" , it)
            contactsViewModel.deleteContact(it)
        }

        recyclerView.adapter = adapter

        floatingActionButton.setOnClickListener {
            startActivityForResult(Intent(this,
                AddContactActivity::class.java),LAUNCH_SECOND_ACTIVITY)
        }

        contactsViewModel.getContacts()
        contactsViewModel.contactList.observe(this, Observer {
            val contactList  = mutableListOf<String>()
            it.forEach {contactList.add(it.number)}
            adapter.setData(contactList)
            Util.blockedNumberList = contactList
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LAUNCH_SECOND_ACTIVITY && resultCode==Activity.RESULT_OK){
            contactsViewModel.insertContact("+91${data?.getStringExtra("result")}")
        }
    }


}
