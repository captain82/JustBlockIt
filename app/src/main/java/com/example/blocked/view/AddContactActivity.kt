package com.example.blocked.view

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
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

        selectFromContact.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
            startActivityForResult(intent, 1)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode==Activity.RESULT_OK && requestCode==1){
            val contactData: Uri? = data?.data
            val c: Cursor = managedQuery(contactData, null, null, null, null)
            if (c.moveToFirst()) {
                val id: String =
                    c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                val hasPhone: String =
                    c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                if (hasPhone.equals("1", ignoreCase = true)) {
                    val phones: Cursor? = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null
                    )
                    phones?.moveToFirst()
                    val contactNumber: String? =
                        phones?.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    editText.setText(contactNumber?.replace("+91","")?.replace(" ",""))
                }
            }
        }
    }
}
