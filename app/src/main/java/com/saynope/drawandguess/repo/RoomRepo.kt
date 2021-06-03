package com.saynope.drawandguess.repo

import android.content.Context
import com.saynope.drawandguess.data.DrawPath
import com.saynope.drawandguess.data.DrawPathPoint
import com.saynope.drawandguess.data.Room
import com.saynope.drawandguess.data.RoomMessage
import com.saynope.drawandguess.utils.GsonUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RoomRepo @Inject constructor(
    private val roomService: RoomService,
    private val userRepo: UserRepo,
    private val context: Context
) {

    private val message = MutableSharedFlow<RoomMessage>()
    private val command = MutableSharedFlow<RoomMessage>()
    private val userMessage = MutableSharedFlow<RoomMessage>()
    private val userCommand = MutableSharedFlow<RoomMessage>()
    private lateinit var room: Room
    val roomCoroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    suspend fun joinRoom(roomId: String, phoneNumber: String): ApiResponse<Room> {
        return roomService.joinRoom(roomId, phoneNumber)
    }

    suspend fun createRoom(userId: String): ApiResponse<Room> {
        return roomService.createRoom(userId)
    }

    fun startReceiveMessage(): Flow<RoomMessage> {
        return message.asSharedFlow()
    }

    fun startReceiveCommand(): Flow<RoomMessage> {
        return command.asSharedFlow()
    }

    fun startReceiveUserCommand(): Flow<RoomMessage> {
        return userCommand.asSharedFlow()
    }

    fun startReceiveUserMessage(): Flow<RoomMessage> {
        return userMessage.asSharedFlow()
    }

    suspend fun onMessage(roomMessage: RoomMessage) {
        message.emit(roomMessage)
    }

    suspend fun onCommand(roomMessage: RoomMessage){
        command.emit(roomMessage)
    }

    suspend fun onUserMessage(roomMessage: RoomMessage){
        userMessage.emit(roomMessage)
    }

    private suspend fun onUserCommand(roomMessage: RoomMessage){
        userCommand.emit(roomMessage)
    }

    fun cacheRoom(room: Room) {
        this.room = room
    }

    fun getCacheRoom(): Room {
        return room
    }

    fun onUserReadyChange(b: Boolean) {
        roomCoroutineScope.launch {
            onUserCommand(buildUserCommandMessage(if(b)RoomMessage.USER_READY else RoomMessage.USER_CANCEL_READY))
        }
    }

    fun onUserDrawOnePathSuccess(drawPath: DrawPath) {
        roomCoroutineScope.launch {
            onUserCommand(buildUserDrawCommandMessage(transForPointToRelated(drawPath)))
        }
    }
    private fun transForPointToRelated(drawPath: DrawPath):DrawPath{
        val list = arrayListOf<DrawPathPoint>()
        for(i in drawPath.path){
            list.add(DrawPathPoint(i.x,i.y))
        }
        val drawPathResult = DrawPath(list,drawPath.width,drawPath.color)
        val path = drawPathResult.path
        val widthPixels = context.resources.displayMetrics.widthPixels
        val heightPixels = context.resources.displayMetrics.density*268
        for(i in path){
            i.x = i.x/widthPixels
            i.y = i.y/heightPixels
        }
        return drawPathResult
    }
    fun transForPointFromRelated(drawPath: DrawPath):DrawPath{
        val list = arrayListOf<DrawPathPoint>()
        for(i in drawPath.path){
            list.add(DrawPathPoint(i.x,i.y))
        }
        val drawPathResult = DrawPath(list,drawPath.width,drawPath.color)
        val path = drawPathResult.path
        val widthPixels = context.resources.displayMetrics.widthPixels
        val heightPixels = context.resources.displayMetrics.density*268
        for(i in path){
            i.x = i.x*widthPixels
            i.y = i.y*heightPixels
        }
        return drawPathResult
    }

    companion object {
        fun buildUserCommandMessage(action: Int): RoomMessage {
            return RoomMessage("", "", "", action)
        }

        fun buildUserDrawCommandMessage(pathPoints: DrawPath):RoomMessage{

            return RoomMessage("",GsonUtils.toJson(pathPoints),"",RoomMessage.USER_DRAW_ONE_PATH)
        }

    }

}