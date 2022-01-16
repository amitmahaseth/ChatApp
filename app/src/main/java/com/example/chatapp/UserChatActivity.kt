package com.example.chatapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.adapter.MessageAdapter
import com.example.chatapp.baseData.BaseActivity
import com.example.chatapp.databinding.ActivityUserChatBinding
import com.example.chatapp.model.UserMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList

class UserChatActivity : BaseActivity() {
    private lateinit var binding: ActivityUserChatBinding
    private lateinit var messageList: ArrayList<UserMessage>
    var receiverRoom: String? = null
    var senderRoom: String? = null
    lateinit var imageUri: Uri
    lateinit var senderUid: String
    lateinit var receiverUid: String


    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        messageList = ArrayList()


        val firstName = intent.getStringExtra("firstName")
        val lastName = intent.getStringExtra("lastName")
        receiverUid = intent.getStringExtra("uId").toString()
        senderUid = FirebaseAuth.getInstance().currentUser?.uid.toString()

        databaseReference = FirebaseDatabase.getInstance().getReference()
        //reference of storage
        firebaseStorage = FirebaseStorage.getInstance()

        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        supportActionBar?.title = firstName + " " + lastName

        binding.chatRecycler.layoutManager = LinearLayoutManager(this)
        val adapter = MessageAdapter(this, messageList)
        binding.chatRecycler.adapter = adapter

        //logic for adding data to recycler
        databaseReference.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postSnapshot in snapshot.children) {
                        val message = postSnapshot.getValue(UserMessage::class.java)
                        messageList.add(message!!)
                    }
                    Log.d("imageUrlReceived", messageList[messageList.size - 1].imageUri.toString())
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        //adding msg to the database
        binding.btnSend.setOnClickListener {

            val message = binding.edtMsgBox.text.toString().trim()
            val messageObject = UserMessage(message, senderUid)

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
        //adding image to the database
        binding.imgShareDocuments.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 25)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 25) {
            if (data?.data != null) {
                imageUri = data.data!!
                val calendar = Calendar.getInstance()
                val storageReference = firebaseStorage.reference.child("chats")
                    .child(calendar.timeInMillis.toString())
                showProgressDialog()
                storageReference.putFile(imageUri).addOnCompleteListener {
                    hideProgressDialog()
                    if (it.isSuccessful) {
                        storageReference.downloadUrl.addOnSuccessListener {
                            val message = binding.edtMsgBox.text.toString().trim()
                            val messageObject = UserMessage(message, senderUid)
                            messageObject.imageUri = it
                            Log.d("imageUrlSent", messageObject.imageUri?.path.toString())
                            databaseReference.child("chats").child(senderRoom!!).child("messages")
                                .push()
                                .setValue(messageObject).addOnSuccessListener {
                                    databaseReference.child("chats").child(receiverRoom!!)
                                        .child("messages")
                                        .push()
                                        .setValue(messageObject)

                                }

                        }
                    }
                }
            }

        }
    }


    private fun empty(): Boolean {
        val edtText = binding.edtMsgBox.text.toString().trim()

        if (edtText.isEmpty()) {
            binding.edtMsgBox.error = "Enter Your Msg"
            return false
        } else {
            return true
        }
    }
}