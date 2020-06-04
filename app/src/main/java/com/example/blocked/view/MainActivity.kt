package com.example.blocked.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.blocked.*
import com.example.blocked.Utils.Util
import com.facebook.stetho.Stetho
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val LAUNCH_SECOND_ACTIVITY = 1
    private val PERMISSION_REQUEST_CODE = 10

    private lateinit var contactsViewModel: ContactsViewModel
    private lateinit var adapter: ContactsRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Stetho.initializeWithDefaults(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decor = window.decorView
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        }

        askPermissisons()
        contactsViewModel = ViewModelProviders.of(this).get(ContactsViewModel::class.java)
        if (contactsViewModel.contactList.value.isNullOrEmpty()) {
            contactsViewModel.getContacts()
        }

        adapter = ContactsRecyclerAdapter {
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

        contactsViewModel.contactList.observe(this, Observer {
            val contactList = mutableListOf<String>()
            val colorList = mutableListOf<Int>()
            it?.forEach {
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

    private fun askPermissisons(){
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
    }
}
