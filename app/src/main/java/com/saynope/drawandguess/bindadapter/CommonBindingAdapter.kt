package com.saynope.drawandguess.bindadapter

import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.google.android.material.textfield.TextInputEditText

@BindingAdapter("isButtonEnable")
fun bindIsButtonEnable(view: View, isButtonEnable: Boolean) {
    view.isEnabled = isButtonEnable
}

@BindingAdapter("afterTextChange")
fun bindAfterTextChange(view: TextInputEditText, block:()->Boolean) {
    view.doAfterTextChanged {
        block.invoke()
    }
}
