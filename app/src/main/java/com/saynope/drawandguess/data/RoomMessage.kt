package com.saynope.drawandguess.data

data class RoomMessage(val fromUserId: String, val msg: String, val toUserId: String,val action:Int) {
    companion object{
        const val JOIN_ROOM = 1
        const val LEAVE_ROOM = 2
        const val TEXT_MESSAGE = 3
        const val PATH_MESSAGE = 4


        const val USER_READY = 201
        const val USER_CANCEL_READY = 202

        const val ROOM_READY = 301
        const val ROOM_WAIT = 302

        const val SERVER_NOTIFY = 101
    }
}