package com.saynope.drawandguess.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.saynope.drawandguess.base.BaseViewModel
import com.saynope.drawandguess.data.User
import com.saynope.drawandguess.repo.UserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepo: UserRepo) : BaseViewModel() {

    val userData: MutableLiveData<User> = MutableLiveData<User>()
    val btnRegisterEnable: MutableLiveData<Boolean> = MutableLiveData(false)

    val nickName = MutableLiveData<String>()
    val phoneNumber = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    fun checkInput(): Boolean {
        var enable = true
        val nickname = this.nickName.value
        val phoneNumber = this.phoneNumber.value
        val password = this.password.value

        if (nickname == null || nickname.isEmpty() || nickname.length > 8) {
            enable = false
        }

        if (phoneNumber == null || phoneNumber.isEmpty() || phoneNumber.length != 11) {
            enable = false
        }

        if (password == null || password.length < 8) {
            enable = false
        }
        btnRegisterEnable.postValue(enable)
        return true
    }

    fun register(nickName: String, phoneNumber: String, password: String) {
        safeCallNet({ userRepo.register(nickName, phoneNumber, password) },
            { t ->
                userData.postValue(t)
                viewModelScope.launch {
                    userRepo.saveUser(
                        User(
                            phoneNumber = t.phoneNumber,
                            nickName = t.nickName,
                            password = t.password,
                            id = t.id
                        )
                    )
                }
            })
    }

}