package com.example.blocked.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.blocked.R
import com.example.blocked.local.ContactModel
import kotlinx.android.synthetic.main.recycler_single_item.view.*


class ContactsRecyclerAdapter(val delete:(String)->Unit):PagedListAdapter<ContactModel,ContactsRecyclerAdapter.ViewHolder>(
    DIFF_CALLBACK) {

    private var contactList = listOf<String>()
    private var colorList = listOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.recycler_single_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.itemView.phoneNumber.text = contactList[position].substringAfter("+91")
        holder.itemView.setBackgroundColor(colorList[position])
        holder.itemView.deleteButton.setOnClickListener {
            delete.invoke(contactList[position])
        }
    }

    fun setData(
        list: List<String>,
        colorList: List<Int>
    ){
        contactList = list
        this.colorList = colorList
        notifyDataSetChanged()
    }

    companion object {
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<ContactModel>() {
            // Concert details may have changed if reloaded from the database,
            // but ID is fixed.
            override fun areItemsTheSame(oldConcert: ContactModel,
                                         newConcert: ContactModel) = oldConcert.number == newConcert.number

            override fun areContentsTheSame(oldConcert: ContactModel,
                                            newConcert: ContactModel) = oldConcert == newConcert
        }
    }

    class ViewHolder(view:View):RecyclerView.ViewHolder(view){
    }

}