package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.adapter.UserAdapter
import com.example.chatapp.databinding.ActivityChatBinding
import com.example.chatapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity(){
    private lateinit var binding: ActivityChatBinding
    private lateinit var userList: ArrayList<User>
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseReference=FirebaseDatabase.getInstance().getReference()
        firebaseAuth= FirebaseAuth.getInstance()
        userList=ArrayList()
        binding.userRecycler.layoutManager=LinearLayoutManager(this)
        val adapter=UserAdapter(this, userList)
        binding.userRecycler.adapter=adapter

        databaseReference.child("Users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (postSnapshot in snapshot.children){
                    val currentUser=postSnapshot.getValue(User::class.java)

                    if (firebaseAuth.currentUser?.uid !=currentUser?.userId){
                        userList.add(currentUser!!)
                    }

                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==R.id.logout){
            //logic for logout
                firebaseAuth.signOut()
            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
            return true
        }
       return true
    }

}