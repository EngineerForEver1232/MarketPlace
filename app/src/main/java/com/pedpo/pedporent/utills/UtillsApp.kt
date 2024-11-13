package com.pedpo.pedporent.utills

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.pedpo.pedporent.R
import dagger.hilt.android.qualifiers.ActivityContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class UtillsApp @Inject constructor(@ActivityContext private val  context: Context) {

    fun checkPhoneNumber(editEmail: EditText):String? {

        var contentNumber =
            NumberFormatPersian.getNewInstance().toNumberEnlish(editEmail.getText().toString())

        if (contentNumber.length in 0..9 || contentNumber.length > 14) {
            Log.i("testnumber", "content.length: $contentNumber")
            editEmail.setError(context.getString(R.string.correctPhone), null)
            editEmail.requestFocus()
            return null
        }

        while (contentNumber.isNotEmpty() && contentNumber[0] == '0') {
            if (contentNumber.isNotEmpty() && contentNumber[0] == '0') {
                contentNumber = contentNumber.removePrefix("0")
            }
        }

        return contentNumber

    }

     fun focusEditText(
        editText: TextInputEditText,
        constraint: ConstraintLayout?
    ) {
        editText.setOnFocusChangeListener { _, b ->
            when (b) {
                true -> {
                    constraint?.visibility = View.GONE
                }
            }
        }
    }

     fun focusTextInputLayout(
        editText: TextInputEditText,
        textInputLayout: TextInputLayout
    ) {
        editText.setOnFocusChangeListener { _, b ->
            when (b) {
                true -> {
                    textInputLayout?.isErrorEnabled = false;
                }
            }
        }
    }

    fun errorEditText(
        inputEditText: TextInputEditText,
        inputLayout: TextInputLayout,
        constraint: ConstraintLayout?
    ): Boolean {
        var bool = if (inputEditText.text.isNullOrEmpty()) {
            inputLayout.setBoxStrokeColorStateList(
                ColorStateList(
                    arrayOf(intArrayOf(android.R.attr.state_focused)),
                    intArrayOf(Color.RED)
                )
            )
            inputLayout.clearFocus()
            constraint?.visibility = View.VISIBLE
            true;
        } else
            false;
//      binding.inputTitle.setBoxStrokeColorStateList(ContextCompat.getColorStateList(this,R.color.selector_input)!!)
//      binding.inputTitle.boxStrokeColor = ContextCompat.getColor(this, R.color.gray_focus_edittext)
        return bool;
    }

    fun startDate(startDate:String) : Calendar {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val calendarStart = Calendar.getInstance()
        calendarStart.time = Date(format.parse(startDate).time)

        return calendarStart
    }
    fun endDate(endDate:String) : Calendar {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val calendarEnd = Calendar.getInstance()
        calendarEnd.time = Date(format.parse(endDate).time)

        return calendarEnd
    }

}