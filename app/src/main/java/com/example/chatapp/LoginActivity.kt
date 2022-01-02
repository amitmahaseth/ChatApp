package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.chatapp.baseData.BaseActivity
import com.example.chatapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth= FirebaseAuth.getInstance()

        binding.tvLogin.setOnClickListener {
            val intent=Intent(this,SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnLogin.setOnClickListener {
            if (login()){
                showProgressDialog()
                firebaseAuth.signInWithEmailAndPassword(binding.edtEmail.text.toString().trim()
                    ,binding.edtPassword.text.toString().trim()).addOnCompleteListener {
                    hideProgressDialog()
                    if (it.isSuccessful){
                        saveLoggedIn(true)
                        val intent=Intent(this,ChatActivity::class.java)
                         startActivity(intent)
                         finish()
                     }else{
                         Toast.makeText(this,"Enter your valid Email And Password",Toast.LENGTH_SHORT).show()
                     }
            }
            }
        }
    }

    private fun login():Boolean{
        val email=binding.edtEmail.text.toString().trim()
        val password=binding.edtPassword.text.toString().trim()

        if (email.isBlank() || password.isBlank()){
            Toast.makeText(this,"Enter your  Email And Password",Toast.LENGTH_SHORT).show()
        return false
        }else{
            return true
        }

    }
}