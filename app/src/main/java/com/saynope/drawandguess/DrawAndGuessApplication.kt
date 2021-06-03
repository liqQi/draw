package com.saynope.drawandguess

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DrawAndGuessApplication :Application(){

    companion object {
        var  myApplicationContext:Application? = null
        fun getContext(): Application {
            return myApplicationContext!!
        }

    }

    override fun onCreate() {
        super.onCreate()
        myApplicationContext = this
    }

}