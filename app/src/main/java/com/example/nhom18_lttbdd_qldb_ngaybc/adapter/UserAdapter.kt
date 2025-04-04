package com.example.nhom18_lttbdd_qldb_ngaybc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nhom18_lttbdd_qldb_ngaybc.R
import com.example.nhom18_lttbdd_qldb_ngaybc.model.User // Đảm bảo import đúng lớp User

class UserAdapter(private val users: List<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.UserName)
        val emailTextView: TextView = itemView.findViewById(R.id.userEmail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.nameTextView.text = user.name
        holder.emailTextView.text = user.email
    }

    override fun getItemCount(): Int {
        return users.size
    }
}