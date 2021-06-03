package com.saynope.drawandguess.di

import android.content.Context
import com.saynope.drawandguess.repo.RoomRepo
import com.saynope.drawandguess.repo.RoomService
import com.saynope.drawandguess.repo.UserRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepoModules {

    @Singleton
    @Provides
    fun provideUserRepo(@ApplicationContext context: Context):UserRepo{
        return UserRepo(context)
    }

    @Singleton
    @Provides
    fun provideRoomRepo(@ApplicationContext context: Context,userRepo: UserRepo,roomService:RoomService):RoomRepo{
        return RoomRepo(roomService,userRepo,context)
    }
}