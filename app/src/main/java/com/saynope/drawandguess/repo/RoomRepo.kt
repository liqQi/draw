package com.saynope.drawandguess.repo

import androidx.databinding.ObservableField
import com.saynope.drawandguess.data.Room
import com.saynope.drawandguess.data.RoomMessage
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RoomRepo @Inject constructor(private val roomService: RoomService) {

    private val message = MutableSharedFlow<RoomMessage>()

    suspend fun joinRoom(roomId: String, phoneNumber: String): ApiResponse<Room> {
        return roomService.joinRoom(roomId, phoneNumber)
    }

    suspend fun createRoom(userId: String): ApiResponse<Room> {
        return roomService.createRoom(userId)
    }

    fun receiveMessage(): Flow<RoomMessage>{
        return message.asSharedFlow()
    }

    suspend fun updateMessage(roomMessage: RoomMessage) {
        message.emit(roomMessage)
    }

}