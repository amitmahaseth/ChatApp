package com.example.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.adapter.MessageAdapter
import com.example.chatapp.adapter.UserAdapter
import com.example.chatapp.databinding.ActivityUserChatBinding
import com.example.chatapp.model.User
import com.example.chatapp.model.UserMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class UserChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserChatBinding
    private lateinit var messageList: ArrayList<UserMessage>
    var receiverRoom:String?=null
    var senderRoom:String?=null

    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUserChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        messageList= ArrayList()


        val firstName=intent.getStringExtra("firstName")
        val lastName=intent.getStringExtra("lastName")
        val receiverUid=intent.getStringExtra("uId")
        val senderUid=FirebaseAuth.getInstance().currentUser?.uid
        databaseReference=FirebaseDatabase.getInstance().getReference()

        senderRoom=receiverUid + senderUid
        receiverRoom=senderUid + receiverUid

        supportActionBar?.title=firstName+" "+lastName

        binding.chatRecycler.layoutManager= LinearLayoutManager(this)
        val adapter= MessageAdapter(this,messageList)
        binding.chatRecycler.adapter=adapter

        //logic for adding data to recycler
        databaseReference.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()

                    for (postSnapshot in snapshot.children){
                        val message=postSnapshot.getValue(UserMessage::class.java)
                            messageList.add(message!!)
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        //adding msg to the database
        binding.btnSend.setOnClickListener {

            val message=binding.edtMsgBox.text.toString().trim()
            val messageObject=UserMessage(message,senderUid)

            if (empty()) {
                databaseReference.child("chats").child(senderRoom!!).child("messages").push()
                    .setValue(messageObject).addOnSuccessListener {
                        databaseReference.child("chats").child(receiverRoom!!).child("messages")
                            .push()
                            .setValue(messageObject)
                    }
                binding.edtMsgBox.text.clear()

            }
        }

    }

    private fun empty():Boolean{
        val edtText=binding.edtMsgBox.text.toString().trim()

        if (edtText.isEmpty()){
            binding.edtMsgBox.error="Enter Your Msg"
            return false
        }else{
            return true
        }
    }
}