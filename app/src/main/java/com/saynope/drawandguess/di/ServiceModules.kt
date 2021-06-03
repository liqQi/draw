package com.saynope.drawandguess.di

import com.saynope.drawandguess.repo.ApiService
import com.saynope.drawandguess.repo.RoomService
import com.saynope.drawandguess.repo.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ServiceModules {

    @Singleton
    @Provides
    fun provideUserService():UserService{
        return ApiService.createUserService()
    }

    @Singleton
    @Provides
    fun provideRoomService():RoomService{
        return ApiService.createRoomService()
    }

}