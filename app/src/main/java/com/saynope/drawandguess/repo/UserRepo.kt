package com.saynope.drawandguess.repo

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.saynope.drawandguess.DrawAndGuessApplication
import com.saynope.drawandguess.data.User
import com.saynope.drawandguess.utils.MD5Utils
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepo @Inject constructor(private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private var isUserLogin: Boolean? = null
    private var cacheUser: User? = null


    companion object {
        val NICK_NAME = stringPreferencesKey("nick_name")
        val PHONE_NUMBER = stringPreferencesKey("phone_number")
        val PASSWORD = stringPreferencesKey("password")
        val USER_ID = longPreferencesKey("id")
    }

    suspend fun isLogin(): Boolean {
        if (isUserLogin == null) {
            val nickName = context.dataStore.data.map { preference ->
                preference[NICK_NAME]
            }.first() ?: ""
            isUserLogin = nickName.isNotEmpty()
        }
        return isUserLogin!!
    }

    suspend fun getUser(): User {
        if (cacheUser == null) {
            val nickName = context.dataStore.data.map { preference ->
                preference[NICK_NAME]
            }.first() ?: ""
            val phoneNumber = context.dataStore.data.map { preference ->
                preference[NICK_NAME]
            }.first() ?: ""
            val password = context.dataStore.data.map { preference ->
                preference[NICK_NAME]
            }.first() ?: ""
            val id = context.dataStore.data.map { preference ->
                preference[USER_ID]
            }.first() ?: 0
            cacheUser = User(nickName, phoneNumber, password, id)
        }
        return cacheUser!!
    }

    suspend fun register(
        nickName: String,
        phoneNumber: String,
        password: String
    ): ApiResponse<User> {
        val mD5 = MD5Utils.MD5(password)
        return ApiService.createUserService().register(nickName, phoneNumber, mD5)
    }

    suspend fun saveUser(user: User) {
        context.dataStore.edit { userSaver ->
            userSaver[NICK_NAME] = user.nickName
            userSaver[PHONE_NUMBER] = user.phoneNumber
            userSaver[PASSWORD] = user.password
            userSaver[USER_ID] = user.id
        }
    }
}