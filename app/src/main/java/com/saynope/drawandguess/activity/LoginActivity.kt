package com.saynope.drawandguess.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.saynope.drawandguess.R
import com.saynope.drawandguess.base.BaseActivity
import com.saynope.drawandguess.databinding.ActivityLoginBinding
import com.saynope.drawandguess.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity() {
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var loginBinding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        loginBinding.loginVideModel = loginViewModel
        registerLoadAndErrorHandleViewModel(loginViewModel)
        initListener()
    }

    private fun initListener() {
        loginViewModel.btnRegisterEnable.observe(this,
            { loginBinding.btnRegister.isEnabled = it })

        loginViewModel.userData.observe(this, {
            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
            finish()
        })

        loginBinding.btnRegister.setOnClickListener {
            run {
                val nickName = loginBinding.nickName.text.toString()
                val phoneNumber = loginBinding.phoneNumber.text.toString()
                val password = loginBinding.password.text.toString()
                loginViewModel.register(nickName, phoneNumber, password)
            }
        }
    }

}