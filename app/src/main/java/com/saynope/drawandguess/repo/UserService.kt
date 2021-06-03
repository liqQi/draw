package com.saynope.drawandguess.repo

import com.saynope.drawandguess.data.User
import retrofit2.http.GET
import retrofit2.http.Query

interface UserService {
    @GET("user/register")
   suspend fun register(@Query("nickName")  nickName:String,@Query("phoneNumber")  phoneNumber:String,@Query("password")  password:String):ApiResponse<User>
}