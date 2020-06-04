package com.example.blocked.view

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.blocked.ContactsViewModel
import com.example.blocked.R
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

            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            val prev: Fragment? = supportFragmentManager.findFragmentByTag("dialog")
            if (prev != null) {
                ft.remove(prev)
            }
            ft.addToBackStack(null)
            val dialogFragment: DialogFragment =
                AddContactDialogFragment()
            dialogFragment.show(ft, "dialog")
            /*startActivityForResult(
                Intent(
                    this,
                    AddContactActivity::class.java
                ), LAUNCH_SECOND_ACTIVITY
            )*/
        }

        contactsViewModel.contactList.observe(this, Observer {
            adapter.submitList(it)
            Log.i("Object" , it.toString())
            val contactList = mutableListOf<String>()
            val colorList = mutableListOf<Int>()
            it?.forEach {
                contactList.add(it.number)
                colorList.add(it.color)
            }

            //adapter.setData(contactList, colorList)
            Util.blockedNumberList = contactList
        })
    }

   /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LAUNCH_SECOND_ACTIVITY && resultCode == Activity.RESULT_OK) {
            Log.i("data" , data?.getStringExtra("result"))
            contactsViewModel.insertContact("+91${data?.getStringExtra("result")}")
        }
    }*/

    private fun askPermissisons() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED || checkSelfPermission(
                    Manifest.permission.CALL_PHONE
                ) == PackageManager.PERMISSION_DENIED ||
                checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED
                || checkSelfPermission(Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_DENIED
                || checkSelfPermission(Manifest.permission.MODIFY_PHONE_STATE) == PackageManager.PERMISSION_DENIED
            ) {
                val permissions = arrayOf<String>(
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.MODIFY_PHONE_STATE
                    )
                requestPermissions(permissions, PERMISSION_REQUEST_CODE)
            }
        }
    }
}
