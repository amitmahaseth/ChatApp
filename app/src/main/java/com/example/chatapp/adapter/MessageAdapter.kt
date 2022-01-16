package com.example.chatapp.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapp.R
import com.example.chatapp.model.UserMessage
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val mContext:Context,val messageList:ArrayList<UserMessage>)
    :RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        val ITEM_RECEIVE=1
        val ITEM_SENT=2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType==1){
            //inflate receive
            val view = LayoutInflater.from(mContext).inflate(R.layout.receive, parent, false)
            return ReceiveViewHolder(view)
        }else{
            //inflate sent
            val view = LayoutInflater.from(mContext).inflate(R.layout.send, parent, false)
            return SentViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentMessage=messageList[position]

        if (holder.javaClass == SentViewHolder::class.java){
            //for sent view holder
            val viewHolder=holder as SentViewHolder
            if (currentMessage.imageUri?.equals("")==true){
                holder.sendImage.visibility=View.VISIBLE
                holder.sentMessage.visibility=View.GONE
                Glide.with(mContext).load(currentMessage.imageUri).into(holder.sendImage)

            }else{
                holder.sentMessage.text=currentMessage.message
                holder.sendImage.visibility=View.GONE
                holder.sentMessage.visibility=View.VISIBLE


            }
        }else{
            //for receive view holder
            val viewHolder=holder as ReceiveViewHolder
            holder.receiveMessage.text=currentMessage.message
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)) {
            return ITEM_SENT
        } else {
                    return ITEM_RECEIVE
        }
    }
    override fun getItemCount(): Int {
        return messageList.size
    }
    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sentMessage=itemView.findViewById<TextView>(R.id.tv_sent)
        val sendImage=itemView.findViewById<ImageView>(R.id.img_send)

    }

    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receiveMessage=itemView.findViewById<TextView>(R.id.tv_receive)
        val receiveImage=itemView.findViewById<ImageView>(R.id.img_receive)
    }

}

