package com.saynope.drawandguess.activity

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.saynope.drawandguess.adapter.MessageAdapter
import com.saynope.drawandguess.base.BaseActivity
import com.saynope.drawandguess.data.RoomMessage
import com.saynope.drawandguess.databinding.ActivityRoomBinding
import com.saynope.drawandguess.service.WebSocketService
import com.saynope.drawandguess.viewmodel.RoomViewModel
import com.saynope.drawandguess.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomActivity : BaseActivity() {

    private lateinit var roomBing: ActivityRoomBinding;

    private val userViewModel:UserViewModel by viewModels()
//    private val roomViewModel:RoomViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        roomBing = ActivityRoomBinding.inflate(layoutInflater)
        setContentView(roomBing.root)
//        roomBing.messageList.layoutManager = LinearLayoutManager(this)
//        val messageAdapter = MessageAdapter()
//        roomBing.messageList.adapter = messageAdapter
//        roomViewModel.roomMsg.observe(this, Observer { msg ->
//            when (msg.action) {
//                RoomMessage.SERVER_NOTIFY -> {
//                    messageAdapter.addRoomMessage(msg)
//                }
//            }
//        })

//        roomViewModel.userReadyStatus.observe(this, Observer { userReady ->
//            roomBing.btnReady.text = if (userReady) "取消准备" else "准备"
//        })
//
//        roomViewModel.roomReadyStatus.observe(this, Observer { roomReady ->
//            roomBing.readyContainer.visibility = if (roomReady) View.INVISIBLE else View.VISIBLE
//        })

//        roomBing.btnReady.setOnClickListener { _ ->
//            val roomMessage = RoomMessage(
//                userViewModel.userData.value?.id.toString(),
//                "",
//                "",
//                if (roomViewModel.userReadyStatus.value == true) RoomMessage.USER_CANCEL_READY else RoomMessage.USER_READY
//            )
//            webServiceConnector.binder.sendMessage(roomMessage)
//        }
//
//        roomBing.drawingBoard.isEnabled = false
//
//        webServiceConnector = WebSocketServiceConnector()
//        bindService(
//            Intent(this, WebSocketService::class.java),
//            webServiceConnector,
//            BIND_AUTO_CREATE
//        )


//        val room = roomViewModel.getRoom()
//        if (room == null) {
//            showToast("ERROR ROM")
//            finish()
//        } else {
//            roomBing.hint.text = room.id
//        }


    }

//    private lateinit var webServiceConnector: WebSocketServiceConnector
//
//    inner class WebSocketServiceConnector : ServiceConnection {
//
//        lateinit var binder: WebSocketService.WebSocketBinder
//
//        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
//            if (service is WebSocketService.WebSocketBinder) {
//                this.binder = service
//            }
//        }
//
//        override fun onServiceDisconnected(name: ComponentName?) {
//
//        }

//    }

    override fun onDestroy() {
        super.onDestroy()
//        unbindService(webServiceConnector)
    }
}