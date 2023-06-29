package com.example.chatapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var mDbRef: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.rv)
        mAuth = FirebaseAuth.getInstance()
        userList = ArrayList()
        mDbRef = FirebaseDatabase.getInstance().getReference()
        mDbRef.child("user").addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for( postSnapshot in snapshot.children ){
                    val currentUser = postSnapshot.getValue(User::class.java)
                    if( mAuth.currentUser!!.uid != currentUser!!.uid ){
                        userList.add(currentUser)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = UserAdapter(this,userList)
        recyclerView.adapter = adapter
    }
}