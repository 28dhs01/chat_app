package com.example.chatapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context: Context, val msgList: ArrayList<Message>) : RecyclerView.Adapter<ViewHolder>(){
    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if( viewType == 1 ){
            val view = LayoutInflater.from(context).inflate(R.layout.item_receive,parent,false)
            return ReceiveViewHolder(view)
        }else{
            val view = LayoutInflater.from(context).inflate(R.layout.item_sent,parent,false)
            return SendViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return msgList.size
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = msgList[position]
        if( FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.sender)){
            return ITEM_SENT
        }else{
            return ITEM_RECEIVE
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentMsg = msgList[position]
        if( holder.javaClass == SendViewHolder::class.java){
            var viewHolder = holder as SendViewHolder
            holder.sentMsg.text = currentMsg.message
        }else{
            var viewHolder = holder as ReceiveViewHolder
            holder.receiveMsg.text = currentMsg.message
        }
    }

    class SendViewHolder(itemView: View) : ViewHolder(itemView){
        val sentMsg = itemView.findViewById<TextView>(R.id.send_msg)
    }
    class ReceiveViewHolder(itemView: View) : ViewHolder(itemView){
        val receiveMsg = itemView.findViewById<TextView>(R.id.receive_msg)

    }
}