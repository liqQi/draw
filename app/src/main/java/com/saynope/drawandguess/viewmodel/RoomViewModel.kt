package com.saynope.drawandguess.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.saynope.drawandguess.base.BaseViewModel
import com.saynope.drawandguess.data.Room
import com.saynope.drawandguess.data.RoomMessage
import com.saynope.drawandguess.repo.ApiResponse
import com.saynope.drawandguess.repo.RoomRepo
import com.saynope.drawandguess.repo.UserRepo
import com.saynope.drawandguess.utils.GsonUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(private val userRepo: UserRepo,private val roomRepo: RoomRepo): BaseViewModel() {

    val roomMsg:MutableLiveData<RoomMessage> = MutableLiveData()
    val userReadyStatus:MutableLiveData<Boolean> = MutableLiveData(false)
    val roomReadyStatus:MutableLiveData<Boolean> = MutableLiveData(false)


//    fun getRoom(): Room? {
//        return roomJoinResponse.value
//    }


//    fun updateRoomStatus(online: Boolean) {
//        roomJoinStatus.postValue(online)
//    }
//
//    fun receiveMessage(message: String) {
//        try {
//            val roomMessage = GsonUtils.fromJson<RoomMessage>(message, RoomMessage::class.java)
//            if(roomMessage.action == RoomMessage.SERVER_NOTIFY){
//                roomMsg.postValue(roomMessage)
//            }else if(roomMessage.action == RoomMessage.ROOM_READY){
//                roomReadyStatus.postValue(true)
//            }
//        } catch (e:Exception){
//
//        }
//    }
//
//    fun onWebSocketError() {
//
//    }

}