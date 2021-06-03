package com.saynope.drawandguess.repo

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiService {

    companion object {
        private const val BASE_URL = "http://192.168.0.101:8080/"
        private val logger =
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }.setLevel(HttpLoggingInterceptor.Level.BODY)

        private val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .callTimeout(30, TimeUnit.SECONDS)
            .build()
        private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        fun createUserService(): UserService {
            return retrofit.create(UserService::class.java)
        }

        fun createRoomService():RoomService{
            return retrofit.create(RoomService::class.java)
        }
    }



}