package com.saynope.drawandguess.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.saynope.drawandguess.data.RoomMessage
import com.saynope.drawandguess.repo.RoomRepo
import com.saynope.drawandguess.repo.UserRepo
import com.saynope.drawandguess.utils.GsonUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
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

    override fun onBind(intent: Intent?): IBinder {
        coroutineScope.launch {
            webSocketConnector =
                RoomWebSocketClient(URI.create(WEB_SOCKET_END + userRepo.getUser().id + "/" + roomRepo.getCacheRoom().id))
            webSocketConnector.connect()
            roomRepo.startReceiveUserCommand().collectLatest {
                webSocketConnector.send(GsonUtils.toJson(it))
            }
            roomRepo.startReceiveUserMessage().collectLatest {
                webSocketConnector.send(GsonUtils.toJson(it))
            }
        }
        return WebSocketBinder()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    inner class WebSocketBinder : Binder()

    inner class RoomWebSocketClient(url: URI) : WebSocketClient(url) {
        override fun onOpen(handshakedata: ServerHandshake?) {

        }

        override fun onMessage(message: String?) {
            if (message == null) {
                println("error message")
            } else {
                Log.d("ws", "onMessage: $message")
                coroutineScope.launch {
                    kotlin.runCatching {
                        try{
                            val roomMessage = GsonUtils.fromJson(message, RoomMessage::class.java)
                            when(roomMessage.action){
                                RoomMessage.SERVER_NOTIFY->
                                    roomRepo.onMessage(roomMessage)
                                RoomMessage.ROOM_READY->
                                    roomRepo.onCommand(roomMessage)
                                RoomMessage.USER_DRAW_ONE_PATH->
                                    roomRepo.onCommand(roomMessage)
                                else -> {}
                            }
                        }catch (e:Exception){
                            e.printStackTrace()
                        }
                    }
                }
            }
        }

        override fun send(text: String?) {
            super.send(text)
            Log.d("ws", "send: $text")
        }

        override fun onClose(code: Int, reason: String?, remote: Boolean) {

        }

        override fun onError(ex: Exception?) {

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }

}