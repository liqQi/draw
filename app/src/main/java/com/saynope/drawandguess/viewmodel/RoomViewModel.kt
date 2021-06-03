package com.saynope.drawandguess.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.saynope.drawandguess.base.BaseViewModel
import com.saynope.drawandguess.data.DrawPath
import com.saynope.drawandguess.data.DrawingUser
import com.saynope.drawandguess.data.Room
import com.saynope.drawandguess.data.RoomMessage
import com.saynope.drawandguess.repo.RoomRepo
import com.saynope.drawandguess.repo.UserRepo
import com.saynope.drawandguess.utils.GsonUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val roomRepo: RoomRepo
) : BaseViewModel() {

    val userReadyStatus: MutableLiveData<Boolean> = MutableLiveData(false)
    val roomReadyStatus: MutableLiveData<Boolean> = MutableLiveData(false)
    val isDrawer: MutableLiveData<Boolean> = MutableLiveData(false)
    val userDrawingPaths: MutableLiveData<DrawPath> = MutableLiveData()

    fun receiveMessage(): Flow<RoomMessage> {
        return roomRepo.startReceiveMessage()
    }

    init {
        receiveCommand()
    }

    private fun receiveCommand() {
        viewModelScope.launch {
            roomRepo.startReceiveCommand().collectLatest { rm ->
                when (rm.action) {
                    RoomMessage.ROOM_READY -> {
                        roomReadyStatus.postValue(true)
                        val drawingUser =
                            GsonUtils.fromJson(rm.msg.replace("\\", ""), DrawingUser::class.java)
                        if (userRepo.getUser().id == drawingUser.drawingUserId.toLong()) {
                            isDrawer.postValue(true)
                        } else {
                            isDrawer.postValue(false)
                        }
                    }
                    RoomMessage.USER_DRAW_ONE_PATH -> {
                        val drawPath: DrawPath =
                            GsonUtils.fromJson(rm.msg.replace("\\", ""), DrawPath::class.java)
                        userDrawingPaths.postValue(roomRepo.transForPointFromRelated(drawPath))
                    }
                }
            }
        }
    }

    fun getRoom(): Room {
        return roomRepo.getCacheRoom()
    }

    fun onUserReadyChange() {
        val userReady = userReadyStatus.value ?: false
        roomRepo.onUserReadyChange(!userReady)
        userReadyStatus.postValue(!userReady)
    }

    fun onUserDrawOnePathSuccess(pathPoints: DrawPath) {
        roomRepo.onUserDrawOnePathSuccess(pathPoints)
    }
}