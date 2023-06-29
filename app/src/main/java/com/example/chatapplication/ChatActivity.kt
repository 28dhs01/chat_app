package com.example.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

private lateinit var chat_rv : RecyclerView
private lateinit var input_text: EditText
private lateinit var send_btn: Button
private lateinit var msgList: ArrayList<Message>
private lateinit var msgAdapter: MessageAdapter
private lateinit var mDbRef: DatabaseReference

private var senderRoom : String? = null
private var receiverRoom: String? = null
class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        senderRoom = receiverUid+senderUid
        receiverRoom = senderUid+receiverUid

        mDbRef = FirebaseDatabase.getInstance().getReference()

        chat_rv = findViewById(R.id.chat_rv)
        input_text = findViewById(R.id.chat_et)
        send_btn = findViewById(R.id.send_btn)
        supportActionBar?.title = name

        msgList = ArrayList()
        msgAdapter = MessageAdapter(this, msgList)
        chat_rv.layoutManager = LinearLayoutManager(this)
        chat_rv.adapter = msgAdapter

//                logic to bring message from our database to app
        mDbRef.child("chat").child(senderRoom!!).child("messages").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                msgList.clear()
                for ( postSnapshot in snapshot.children ){
                    val message = postSnapshot.getValue(Message::class.java)
                    msgList.add(message!!)
                }
                msgAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        send_btn.setOnClickListener {
            val msg = input_text.text.toString()
            val msgObject = Message(msg,senderUid)
            mDbRef.child("chat").child(senderRoom!!).child("messages").push().setValue(msgObject)
                .addOnSuccessListener {
                    mDbRef.child("chat").child(receiverRoom!!).child("messages").push().setValue(msgObject)
                }
            input_text.setText("")
        }

    }
}