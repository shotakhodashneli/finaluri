package com.example.hunting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.hunting.databinding.ActivityRegistrationBinding
import com.example.hunting.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private val auth = FirebaseAuth.getInstance()

    private val dbUsers = FirebaseDatabase.getInstance().getReference("users")

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

    }

    private fun init() {
        binding.buttonAuthorization.setOnClickListener {
            finish()
        }

        binding.buttonRegistration.setOnClickListener {

            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Log.d("SHOW", "Error")
                    }
                }.addOnSuccessListener {
                    val name = binding.editTextName.text.toString()
                    val surname = binding.editTextSurname.text.toString()
                    val user = User(name, surname)

                    dbUsers.child(auth.currentUser!!.uid).setValue(user)
                }

        }
    }

}