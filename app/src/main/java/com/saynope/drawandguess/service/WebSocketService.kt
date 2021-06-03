package com.saynope.drawandguess.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.saynope.drawandguess.data.Room
import com.saynope.drawandguess.data.RoomMessage
import com.saynope.drawandguess.repo.RoomRepo
import com.saynope.drawandguess.repo.UserRepo
import com.saynope.drawandguess.utils.GsonUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import javax.inject.Inject

@AndroidEntryPoint
class WebSocketService : Service() {

    companion object {
        const val WEB_SOCKET_END: String = "ws://192.168.0.101:8080/ws/"
    }

    private lateinit var webSocketConnector: RoomWebSocketClient

    @Inject
    lateinit var userRepo: UserRepo

    @Inject
    lateinit var roomRepo: RoomRepo

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onBind(intent: Intent?): IBinder? {
        return WebSocketBinder()
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val stringExtra = intent?.getStringExtra("ROOM")
        if (stringExtra != null) {
            val room: Room = GsonUtils.fromJson(stringExtra, Room::class.java)
            coroutineScope.launch {
                webSocketConnector =
                    RoomWebSocketClient(URI.create(WEB_SOCKET_END + userRepo.getUser()?.id + "/" + room.id))
                webSocketConnector.connect()
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    inner class WebSocketBinder : Binder() {
        fun sendMessage(msg: RoomMessage) {
            webSocketConnector.send(GsonUtils.toJson(msg))
        }
    }


    inner class RoomWebSocketClient(url: URI) : WebSocketClient(url) {
        override fun onOpen(handshakedata: ServerHandshake?) {
            coroutineScope.launch {
                repeat(5) {
                    delay(1000)
                    val roomMessage = RoomMessage("1", "加入房间成功", "2", RoomMessage.SERVER_NOTIFY)
                    roomRepo.updateMessage(roomMessage)
                }
            }
        }

        override fun onMessage(message: String?) {
            if (message == null) {
                println("error message")
            } else {
//                roomViewModel.receiveMessage(message)
                println(message)
            }
        }

        override fun onClose(code: Int, reason: String?, remote: Boolean) {
//            roomViewModel.updateRoomStatus(false)
        }

        override fun onError(ex: Exception?) {
//            roomViewModel.updateRoomStatus(false)
//            roomViewModel.onWebSocketError()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }

}