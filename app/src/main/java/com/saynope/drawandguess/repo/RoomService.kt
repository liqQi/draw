package com.saynope.drawandguess.repo

import com.saynope.drawandguess.data.Room
import retrofit2.http.GET
import retrofit2.http.Query

interface RoomService {

    @GET("room/join")
    suspend fun joinRoom(@Query("roomId") roomId: String,@Query("userId") phoneNumber: String):ApiResponse<Room>
    @GET("room/create")
    suspend fun createRoom(@Query("userId")userId: String): ApiResponse<Room>
}