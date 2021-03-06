package com.example.blocked.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.blocked.ContactsViewModel
import com.example.blocked.R
import com.facebook.stetho.Stetho
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val PERMISSION_REQUEST_CODE = 10
    private lateinit var contactsViewModel: ContactsViewModel
    private lateinit var adapter: ContactsRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Stetho.initializeWithDefaults(this)

        val serviceIntent = Intent(this,CallBlockService::class.java)
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            startForegroundService(serviceIntent)
        }else
        {
            startService(serviceIntent)
        }
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
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            val prev: Fragment? = supportFragmentManager.findFragmentByTag("dialog")
            if (prev != null) {
                ft.remove(prev)
            }
            ft.addToBackStack(null)
            val dialogFragment: DialogFragment =
                AddContactDialogFragment()
            dialogFragment.show(ft, "dialog")
        }

        contactsViewModel.contactList.observe(this, Observer {
            adapter.submitList(it)
            val contactList = mutableListOf<String>()
            it?.forEach {
                contactList.add(it.number)
            }
        })
    }

    private fun askPermissisons() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED || checkSelfPermission(
                    Manifest.permission.CALL_PHONE
                ) == PackageManager.PERMISSION_DENIED ||
                checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED
                || checkSelfPermission(Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_DENIED
                || checkSelfPermission(Manifest.permission.ANSWER_PHONE_CALLS) == PackageManager.PERMISSION_DENIED
                || checkSelfPermission(Manifest.permission.MODIFY_PHONE_STATE) == PackageManager.PERMISSION_DENIED
            ) {
                val permissions = arrayOf<String>(
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.MODIFY_PHONE_STATE,
                    Manifest.permission.ANSWER_PHONE_CALLS
                )
                requestPermissions(permissions, PERMISSION_REQUEST_CODE)
            }
        }
    }
}
