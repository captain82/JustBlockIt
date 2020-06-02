package com.example.blocked

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_single_item.view.*


class ContactsRecyclerAdapter(val delete:(String)->Unit):RecyclerView.Adapter<ContactsRecyclerAdapter.ViewHolder>() {

    private var contactList = listOf<String>()
    private var colorList = listOf<Int>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_single_item,parent,false))
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

    class ViewHolder(view:View):RecyclerView.ViewHolder(view)

}