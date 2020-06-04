package com.example.blocked.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.blocked.R
import com.example.blocked.local.ContactModel
import kotlinx.android.synthetic.main.recycler_single_item.view.*
import org.w3c.dom.Text


class ContactsRecyclerAdapter(val delete: (String) -> Unit) :
    PagedListAdapter<ContactModel, ContactsRecyclerAdapter.ViewHolder>(
        DIFF_CALLBACK
    ) {

   /* private var contactList = listOf<String>()
    private var colorList = listOf<Int>()*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val contact = getItem(position)

        with(holder) {
            bindTo(contact)
            holder.itemView.deleteButton.setOnClickListener {
                contact?.number?.let { it1 -> delete.invoke(it1) }
            }
        }

/*
        holder.itemView.phoneNumber.text = contactList[position].substringAfter("+91")
        holder.itemView.setBackgroundColor(colorList[position])
        holder.itemView.deleteButton.setOnClickListener {
            delete.invoke(contactList[position])
        }*/
    }

   /* fun setData(
        list: List<String>,
        colorList: List<Int>
    ) {
        contactList = list
        this.colorList = colorList
        notifyDataSetChanged()
    }*/

    companion object {
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<ContactModel>() {
            override fun areItemsTheSame(
                oldConcert: ContactModel,
                newConcert: ContactModel
            ) = oldConcert.number == newConcert.number

            override fun areContentsTheSame(
                oldConcert: ContactModel,
                newConcert: ContactModel
            ) = oldConcert == newConcert
        }
    }

    class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.recycler_single_item, parent, false)
    ) {
        private var contact: ContactModel? = null
        private val phoneEditText = itemView.findViewById<TextView>(R.id.phoneNumber)

        fun bindTo(contact: ContactModel?) {
            this.contact = contact
            phoneEditText.text = contact?.number?.substringAfter("+91")
            contact?.color?.let { itemView.setBackgroundColor(it) }
        }


    }

}