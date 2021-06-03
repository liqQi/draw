package com.saynope.drawandguess.data

data class RoomMessage(val fromUserId: String, val msg: String, val toUserId: String,val action:Int) {
    companion object{
        const val SERVER_NOTIFY = 101

        const val USER_READY = 201
        const val USER_CANCEL_READY = 202

        const val ROOM_READY = 301
        const val ROOM_WAIT = 302

        const val USER_DRAW_ONE_PATH = 401
    }
}