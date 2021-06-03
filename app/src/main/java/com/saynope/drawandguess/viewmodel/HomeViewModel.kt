package com.saynope.drawandguess.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.saynope.drawandguess.base.BaseViewModel
import com.saynope.drawandguess.data.Room
import com.saynope.drawandguess.data.RoomMessage
import com.saynope.drawandguess.data.User
import com.saynope.drawandguess.repo.ApiResponse
import com.saynope.drawandguess.repo.RoomRepo
import com.saynope.drawandguess.repo.UserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val roomRepo: RoomRepo
) : BaseViewModel() {

    val user = MutableLiveData<User>()


    val roomJoinStatus: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val roomJoinResponse: MutableLiveData<Room> = MutableLiveData()

    init {
        viewModelScope.launch {
            user.postValue(userRepo.getUser())
        }
    }

    fun receiveMessage():Flow<RoomMessage>{
        return  roomRepo.receiveMessage()
    }


    fun createRoom() {
        safeCallNet(
            { roomRepo.createRoom(user.value?.id.toString()) },
            { t -> roomJoinResponse.postValue(t) })
    }


    fun joinRoom(roomId: String) {
        safeCallNet(
            { roomRepo.joinRoom(roomId, user.value?.id.toString()) },
            { t -> roomJoinResponse.postValue(t) })
    }


}