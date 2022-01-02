package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.chatapp.baseData.BaseActivity
import com.example.chatapp.databinding.ActivitySignBinding
import com.example.chatapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : BaseActivity() {
    private lateinit var binding: ActivitySignBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySignBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth= FirebaseAuth.getInstance()

        binding.btnNext.setOnClickListener {
        if (signUpUser()){
            showProgressDialog()
           firebaseAuth.createUserWithEmailAndPassword(binding.edtEmail.text.toString().trim()
                ,binding.edtPass.text.toString().trim()).addOnCompleteListener {
               savedUserData()
               if (it.isSuccessful){

               }else{
                   hideProgressDialog()
                   Toast.makeText(this,"Email and password can't be blank",Toast.LENGTH_SHORT).show()
               }
           }

        }

        }
        binding.tvLogin.setOnClickListener {
            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

   private fun signUpUser():Boolean{
       val firstName:String=binding.edtFirst.text.toString().trim()
       val lastName:String=binding.edtLast.text.toString().trim()
       val email:String=binding.edtEmail.text.toString().trim()
       val phoneNumber:String=binding.edtPhone.text.toString().trim()
       val password:String=binding.edtPass.text.toString().trim()

       if (firstName.isBlank()){
           binding.edtFirst.error="enter your first name"
           return false
       }else
           if (lastName.isBlank()){
               binding.edtLast.error="enter your last name"
               return false
           }else
               if (email.isBlank()){
                    binding.edtEmail.error="enter your email"
                   return false
               }else
              if (!(emailValidation(email))){
                  Toast.makeText(this,"Your mail is not vaild",Toast.LENGTH_SHORT).show()
                  return false
              }else
                  if (phoneNumber.isBlank()){
                      binding.edtPhone.error="enter your phone number"
                      return false
                  }else
                      if (password.isBlank()){
                          binding.edtPass.error="enter your password"
                          return false
                      }else{
                          return true
                      }

   }
    fun emailValidation(email:String):Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun savedUserData(){
        val firstName=binding.edtFirst.text.toString().trim()
        val lastName=binding.edtLast.text.toString().trim()
        val userEmail=binding.edtEmail.text.toString().trim()
        val userPhone=binding.edtPhone.text.toString().trim()
        val userId=firebaseAuth.currentUser?.uid

        database=FirebaseDatabase.getInstance().getReference("Users")
        val user= User(firstName, lastName, userEmail, userPhone,userId)
        database.child(userId.toString()).setValue(user).addOnSuccessListener {
            hideProgressDialog()
            saveLoggedIn(true)
            Toast.makeText(this, "Successfully", Toast.LENGTH_SHORT).show()
            val intent=Intent(this,ChatActivity::class.java)
            startActivity(intent)
            finish()

        }.addOnFailureListener {
            hideProgressDialog()
            Toast.makeText(this, "UnSuccessfully", Toast.LENGTH_SHORT).show()

        }
    }
}