package com.example.chatapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.R
import com.example.chatapp.UserChatActivity
import com.example.chatapp.model.User

class UserAdapter(val mContext: Context, val userList: ArrayList<User>) :
    RecyclerView.Adapter<UserViewAdapter>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewAdapter {
        val view = LayoutInflater.from(mContext).inflate(R.layout.user_list, parent, false)
        return UserViewAdapter(view)
    }

    override fun onBindViewHolder(holder: UserViewAdapter, position: Int) {
        val currentUser = userList[position]
        holder.tvUserName.text = StringBuilder().apply {
            append(currentUser.firstName)
            append(" ")
            append(currentUser.lastName)
        }.toString()

        holder.itemView.setOnClickListener {
            val intent= Intent(mContext,UserChatActivity::class.java)

            intent.putExtra("firstName",currentUser.firstName)
            intent.putExtra("lastName",currentUser.lastName)
            intent.putExtra("uId",currentUser.userId)

            mContext.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}



class UserViewAdapter(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvUserName = itemView.findViewById<TextView>(R.id.tv_user_list)

}
