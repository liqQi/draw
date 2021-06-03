package com.saynope.drawandguess.activity

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.saynope.drawandguess.R
import com.saynope.drawandguess.base.BaseActivity
import com.saynope.drawandguess.databinding.ActivityHomeBinding
import com.saynope.drawandguess.service.WebSocketService
import com.saynope.drawandguess.utils.GsonUtils
import com.saynope.drawandguess.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : BaseActivity() {

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var homeBinding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        homeViewModel.user.observe(this, {
            homeBinding.homeViewModel = homeViewModel
        })
        registerLoadAndErrorHandleViewModel(homeViewModel)
        homeViewModel.roomJoinResponse.observe(this, { room ->
            val intent = Intent(this, WebSocketService::class.java)
            intent.putExtra("ROOM", GsonUtils.toJson(room))
            startService(intent)
        })
        homeViewModel.roomJoinStatus.observe(this, { join ->
            if (join) {
//                startActivity(Intent(this@HomeActivity, RoomActivity::class.java))
            }
        })
        initView()
    }

    private fun initView() {

        homeBinding.joinRoom.setOnClickListener { _ ->
            showJoinRoomDialog()
        }

        homeBinding.createRoom.setOnClickListener { _ ->
            homeViewModel.createRoom()
        }
        lifecycleScope.launch {
            homeViewModel.receiveMessage().collectLatest {
                println(GsonUtils.toJson(it))
            }
        }

    }

    private fun showJoinRoomDialog() {
        MaterialDialog(this).show {
            input(
                hintRes = R.string.input_room_hint,
                inputType = InputType.TYPE_CLASS_NUMBER,
                maxLength = 6
            ) { _, charSequence ->
                if (charSequence.length != 6) {
                    showToast("请输入正确的房间号")
                }
                joinRoom(charSequence.toString())
                dismiss()
            }
            positiveButton { R.string.submit }
        }
    }

    private fun joinRoom(roomId: String) {
        homeViewModel.joinRoom(roomId)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, WebSocketService::class.java))
    }


}