package com.example.blocked

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val LAUNCH_SECOND_ACTIVITY = 1
    private val PERMISSION_REQUEST_CODE = 10

    private lateinit var contactsViewModel: ContactsViewModel
    private lateinit var adapter: ContactsRecyclerAdapter

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

        contactsViewModel = ViewModelProviders.of(this).get(ContactsViewModel::class.java)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED || checkSelfPermission(
                    Manifest.permission.CALL_PHONE
                ) == PackageManager.PERMISSION_DENIED
            ) {
                val permissions = arrayOf<String>(
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.CALL_PHONE
                )
                requestPermissions(permissions, PERMISSION_REQUEST_CODE)
            }
        }
        adapter = ContactsRecyclerAdapter {
            Log.i("Delete", it)
            contactsViewModel.deleteContact(it)
        }

        recyclerView.adapter = adapter

        floatingActionButton.setOnClickListener {
            startActivityForResult(
                Intent(
                    this,
                    AddContactActivity::class.java
                ), LAUNCH_SECOND_ACTIVITY
            )
        }

        if (contactsViewModel.contactList.value.isNullOrEmpty()) {
            contactsViewModel.getContacts()
            Log.i("Rotate", "onCreate")

        }
        contactsViewModel.contactList.observe(this, Observer {
            Log.i("Rotate", "hanges")
            val contactList = mutableListOf<String>()
            val colorList = mutableListOf<Int>()
            it.forEach {
                contactList.add(it.number)
                colorList.add(it.color)
            }
            adapter.setData(contactList, colorList)
            Util.blockedNumberList = contactList
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LAUNCH_SECOND_ACTIVITY && resultCode == Activity.RESULT_OK) {
            contactsViewModel.insertContact("+91${data?.getStringExtra("result")}")
        }
    }
}
