package com.saynope.drawandguess.activity

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.saynope.drawandguess.R
import com.saynope.drawandguess.adapter.MessageAdapter
import com.saynope.drawandguess.base.BaseActivity
import com.saynope.drawandguess.databinding.ActivityRoomBinding
import com.saynope.drawandguess.service.WebSocketService
import com.saynope.drawandguess.utils.GsonUtils
import com.saynope.drawandguess.viewmodel.RoomViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RoomActivity : BaseActivity() {

    private lateinit var roomBing: ActivityRoomBinding
    private val roomViewModel: RoomViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        roomBing = DataBindingUtil.setContentView(this, R.layout.activity_room)
        roomBing.messageList.layoutManager = LinearLayoutManager(this)
        val messageAdapter = MessageAdapter()
        roomBing.messageList.adapter = messageAdapter
        lifecycleScope.launch {
            roomViewModel.receiveMessage().collectLatest {
                println(GsonUtils.toJson(it))
            }
        }

        roomViewModel.userReadyStatus.observe(this, { userReady ->
            roomBing.btnReady.text = if (userReady) "取消准备" else "准备"
        })

        roomViewModel.roomReadyStatus.observe(this, { roomReady ->
            roomBing.readyContainer.visibility = if (roomReady) View.INVISIBLE else View.VISIBLE
        })

        roomBing.btnReady.setOnClickListener { _ ->
            roomViewModel.onUserReadyChange()
        }
        webServiceConnector = WebSocketServiceConnector()
        bindService(
            Intent(this, WebSocketService::class.java),
            webServiceConnector,
            BIND_AUTO_CREATE
        )
        roomBing.drawingBoard.isEnabled = false
        roomBing.hint.text = roomViewModel.getRoom().id
        lifecycleScope.launch {
            roomViewModel.receiveMessage().collectLatest {
                messageAdapter.addRoomMessage(it)
            }
        }

        roomViewModel.isDrawer.observe(this, { isDrawer ->
            roomBing.drawingBoard.isEnabled = isDrawer
        })

        roomBing.drawingBoard.addOnDrawPathFinishCallback { pathPoints ->
            roomViewModel.onUserDrawOnePathSuccess(pathPoints)

        }

        roomViewModel.userDrawingPaths.observe(this, {
            roomBing.drawingBoard.onReceiveNetDrawPath(it)
        })
    }

    private lateinit var webServiceConnector: WebSocketServiceConnector

    inner class WebSocketServiceConnector : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(webServiceConnector)
    }
}