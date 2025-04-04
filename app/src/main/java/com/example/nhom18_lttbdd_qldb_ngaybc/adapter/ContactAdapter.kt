package com.example.nhom18_lttbdd_qldb_ngaybc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nhom18_lttbdd_qldb_ngaybc.R
import com.example.nhom18_lttbdd_qldb_ngaybc.model.Contact

class ContactAdapter(private val contacts: List<Contact>) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.contactName)
        val phoneTextView: TextView = itemView.findViewById(R.id.contactPhone)
        val emailTextView: TextView = itemView.findViewById(R.id.contactEmail)
        val addressTextView: TextView = itemView.findViewById(R.id.contactAddress)
        val notesTextView: TextView = itemView.findViewById(R.id.contactNotes)
        val favoriteTextView: TextView = itemView.findViewById(R.id.contactFavorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.nameTextView.text = contact.name
        holder.phoneTextView.text = contact.phone ?: "Không có số điện thoại"
        holder.emailTextView.text = contact.email ?: "Không có email"
        holder.addressTextView.text = contact.address ?: "Không có địa chỉ"
        holder.notesTextView.text = contact.notes ?: "Không có ghi chú"
        holder.favoriteTextView.text = if (contact.isFavorite) "Yêu thích" else "Không yêu thích"
    }

    override fun getItemCount(): Int {
        return contacts.size
    }
}