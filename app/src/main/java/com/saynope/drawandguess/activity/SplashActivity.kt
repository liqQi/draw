package com.saynope.drawandguess.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.saynope.drawandguess.R
import com.saynope.drawandguess.base.BaseActivity
import com.saynope.drawandguess.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            delay(2000)
            launch {
                val value = userViewModel.checkLogin()
                if (value) {
                    startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                } else {
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                }
                finish()
            }
        }
    }

}