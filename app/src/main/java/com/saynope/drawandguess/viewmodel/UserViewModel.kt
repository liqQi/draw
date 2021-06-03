package com.saynope.drawandguess.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.saynope.drawandguess.base.BaseViewModel
import com.saynope.drawandguess.data.User
import com.saynope.drawandguess.repo.ApiResponse
import com.saynope.drawandguess.repo.UserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userRepo: UserRepo) : BaseViewModel() {

    suspend fun checkLogin() :Boolean{
       return userRepo.isLogin()
    }
}