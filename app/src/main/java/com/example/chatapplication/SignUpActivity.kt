package com.example.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {
    private lateinit var et_username: EditText
    private lateinit var et_mail: EditText
    private lateinit var et_password: EditText
    private lateinit var btn_signup: Button
    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        et_username = findViewById(R.id.username)
        et_mail = findViewById(R.id.et_mail)
        et_password = findViewById(R.id.et_password)
        btn_signup = findViewById(R.id.sign_up)
        mAuth = FirebaseAuth.getInstance()

        btn_signup.setOnClickListener {
            val name = et_username.text.toString()
            val mail = et_mail.text.toString()
            val password = et_password.text.toString()
            signUp(name,mail,password)
        }
    }

    private fun signUp(name: String, mail: String, password: String) {
//        logic for sign up

        mAuth.createUserWithEmailAndPassword(mail, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    addUserToDatabase(name,mail,mAuth.uid)

                    val intent = Intent(this@SignUpActivity,MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.

                    Toast.makeText(this@SignUpActivity, "Authentication failed.$mail - $password", Toast.LENGTH_SHORT,).show()
                }
            }
    }

    private fun addUserToDatabase(name: String, mail: String, uid: String?) {
        mDbRef = FirebaseDatabase.getInstance().getReference()
        mDbRef.child("user").child(uid.toString()).setValue(User(name,mail, uid.toString()))
    }
}