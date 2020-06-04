package com.example.blocked.view


import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.blocked.ContactsViewModel
import com.example.blocked.R

/**
 * A simple [Fragment] subclass.
 */
class AddContactDialogFragment : AppCompatDialogFragment() {

    private val contactsViewModel: ContactsViewModel by lazy {
        ViewModelProviders.of(this).get(ContactsViewModel::class.java)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)
        val inflater = activity?.layoutInflater
        val view = inflater?.inflate(R.layout.dialog_fragment, null)
        builder.setView(view)

        view?.findViewById<Button>(R.id.saveButton)?.setOnClickListener {
            val phoneNumber = view.findViewById<EditText>(R.id.editText)
            if (phoneNumber.text.isNotEmpty()){
                addPhoneNumber(phoneNumber.text.toString())
            }
        }
        view?.findViewById<TextView>(R.id.selectFromContact)?.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
            startActivityForResult(intent, 1)
        }
        return builder.create()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            val contactData: Uri? = data?.data
            val c: Cursor? = activity?.managedQuery(contactData, null, null, null, null)
            if (c?.moveToFirst()!!) {
                val id: String =
                    c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                val hasPhone: String =
                    c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                if (hasPhone.equals("1", ignoreCase = true)) {
                    val phones: Cursor? = activity?.contentResolver?.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null
                    )
                    phones?.moveToFirst()
                    val contactNumber: String? =
                        phones?.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    Log.i("number", contactNumber.toString())
                    if (contactNumber != null) {
                        addPhoneNumber(contactNumber.replace("+91", "").replace(" ", ""))
                    }
                }
            }
        }
    }

    private fun addPhoneNumber(phoneNumber: String) {
        contactsViewModel.insertContact("+91${phoneNumber}")
        dismiss()
    }

}
