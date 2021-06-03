package com.saynope.drawandguess.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.saynope.drawandguess.data.RoomMessage

class MessageAdapter() : RecyclerView.Adapter<MessageHolder>() {

    companion object {
        const val TYPE_SYSTEM = 1
        const val TYPE_USER = 2
    }

    private val msgs = ArrayList<RoomMessage>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        val text = TextView(parent.context)
        text.layoutParams = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        text.setPadding(0, 20, 0, 20)
        return MessageHolder(text)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        if (holder.itemView is TextView) {
            val roomMessage = msgs[position]
            when (getItemViewType(position)) {
                TYPE_USER -> {
                    holder.itemView.text =  roomMessage.msg
                    holder.itemView.setTextColor(Color.parseColor("#33000000"))
                }

                TYPE_SYSTEM -> {
                    holder.itemView.text = roomMessage.msg
                    holder.itemView.setTextColor(Color.parseColor("#F57F17"))
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return msgs.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (msgs[position].action == RoomMessage.SERVER_NOTIFY) TYPE_SYSTEM else TYPE_USER
    }

    fun addRoomMessage(msg: RoomMessage) {
        msgs.add(msg)
        notifyItemInserted(msgs.size - 1)
    }
}