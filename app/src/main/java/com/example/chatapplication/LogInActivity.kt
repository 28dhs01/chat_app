package com.example.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LogInActivity : AppCompatActivity() {
    private lateinit var et_mail : EditText
    private lateinit var et_password : EditText
    private lateinit var btn_login : Button
    private lateinit var btn_signup : Button
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        et_mail = findViewById(R.id.et_mail)
        et_password = findViewById(R.id.et_password)
        btn_login = findViewById(R.id.sign_in)
        btn_signup = findViewById(R.id.sign_up)
        mAuth = FirebaseAuth.getInstance()
        btn_signup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        btn_login.setOnClickListener {
            val mail = et_mail.text.toString()
            val password = et_password.text.toString()
            login(mail,password)
        }
    }
    private fun login(mail: String , password: String){
//        logic for login
        mAuth.signInWithEmailAndPassword(mail, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val intent = Intent(this@LogInActivity,MainActivity::class.java)
                    startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this@LogInActivity, "No account found. SignUp first!", Toast.LENGTH_SHORT,).show()
                }
            }
    }
}