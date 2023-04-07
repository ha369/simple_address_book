package com.example.simple_address_book

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactsAdapter(
    private val context: Context,
    private val contactsList: ArrayList<Contact>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onEditClick(position: Int)
        fun onDeleteClick(position: Int)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.name_text_view)
        val phoneTextView: TextView = itemView.findViewById(R.id.phone_text_view)
        val addressTextView: TextView = itemView.findViewById(R.id.address_text_view)
        val editButton: ImageButton = itemView.findViewById(R.id.edit_button)
        val deleteButton: ImageButton = itemView.findViewById(R.id.delete_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contact_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentContact = contactsList[position]
        holder.nameTextView.text = currentContact.name
        holder.phoneTextView.text = currentContact.phone
        holder.addressTextView.text = currentContact.address

        holder.editButton.setOnClickListener {
            listener.onEditClick(position)
        }

        holder.deleteButton.setOnClickListener {
            listener.onDeleteClick(position)
        }
    }

    override fun getItemCount() = contactsList.size
}
