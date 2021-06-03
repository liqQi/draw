package com.saynope.drawandguess.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.saynope.drawandguess.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {

    private val mActivityProvider: ViewModelProvider by lazy {
        ViewModelProvider(this)
    }

    protected open fun <T : ViewModel> getActivityScopeViewModel(modelClass: Class<T>): T {
        return mActivityProvider.get(modelClass)
    }

    private var progressDialog:AlertDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun registerLoadAndErrorHandleViewModel(viewModel: BaseViewModel){
        viewModel.loadingState.observe(this, Observer {isLoading->
            if(isLoading){
                showLoadingProgress()
            }else{
                hideLoadingProgress()
            }
        })

        viewModel.requestResultState.observe(this, Observer {
            showToast(it.message)
        })
    }

    private fun showLoadingProgress() {
        progressDialog =
            MaterialAlertDialogBuilder(this).setMessage(R.string.loading).setCancelable(false)
                .show()
    }

    private fun hideLoadingProgress(){
        if(progressDialog!=null && progressDialog!!.isShowing){
            progressDialog!!.hide()
        }
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}