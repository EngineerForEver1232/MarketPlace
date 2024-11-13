package com.pedpo.pedporent.utills

import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.pedpo.pedporent.utills.language.PrefManagerLanguage
import java.text.DecimalFormat
import javax.inject.Inject

class NumberFormatDigits @Inject constructor(private val prefManagerLanguage: PrefManagerLanguage) {

    private val faNumbers = arrayOf("۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹")

    fun convertToDigist(number: Long): String? {

        if (number.toString().isEmpty())
            return ""

        var number = number.toString().replace("[^\\d]".toRegex(), "").toLong()

        val df = DecimalFormat("#,###")
        return if (prefManagerLanguage.language == ContextApp.EN) {
            df.format(number)
        } else
            convertToPersian(df.format(number));
    }

    fun clearNumber(number: String?): String? {
        if (number.toString().isEmpty())
            return "";
//        return number.toString().replace("[^\\d]".toRegex(), "");
        return toNumberEnlish(number.toString().replace("[^\\d]".toRegex(), ""));

    }

    fun convertDigistForEdittext(number: String): String? {
        if (number.toString().isEmpty())
            return "";

        var number = number.toString().replace("[^\\d]".toRegex(), "").toLong();

        val df = DecimalFormat("#,###")
        return if (prefManagerLanguage.language == ContextApp.EN) {
            df.format(number)
        } else
            convertToPersian(df.format(number));
    }


    fun convertToPersian(text: String): String? {
        if (text.isEmpty()) {
            return ""
        }
        var out: String? = ""
        val length = text.length
        for (i in 0 until length) {
            val c = text[i]
            when (c) {
                in '0'..'9' -> {
                    val number = c.toString().toInt()
                    out += faNumbers[number]
                }
                '٫', ',', '٬' -> {
                    out += '،'
                }
                else -> {
                    out += c
                }
            }
        }
        return out
    }

    var textWatcher: TextWatcher? = null;

    fun convertEditTextNumber(
        editText: EditText,
        prefManagerLanguage: PrefManagerLanguage,
        length: Int
    ) {
        var numberFormatDigits = NumberFormatDigits(prefManagerLanguage);

        textWatcher = object : TextWatcher {
            private var onTextChanged = 0
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(s: CharSequence, i: Int, i1: Int, i2: Int) {

                if (s.toString().length < length) {

                    val formatNumber =
                        numberFormatDigits?.convertDigistForEdittext((editText.text.toString()))
//                    editText.setText(formatNumber)

                    editText.removeTextChangedListener(textWatcher);
                    editText.setText(formatNumber)
                    editText.addTextChangedListener(textWatcher);
                }
            }

            override fun afterTextChanged(editable: Editable) {

                editText.setSelection(editText.text.length)

            }
        }

        editText.addTextChangedListener(textWatcher)
    }

    private val handler = Handler(
        Looper.getMainLooper()
    ) { false }

    fun toNumberEnlish(str: String): String? {
        return str
            .replace("۰", "0")
            .replace("۱", "1")
            .replace("۲", "2")
            .replace("۳", "3")
            .replace("۴", "4")
            .replace("۵", "5")
            .replace("۶", "6")
            .replace("۷", "7")
            .replace("۸", "8")
            .replace("۹", "9")
    }

    fun toNumberPersian(str: String): String? {
        return str
            .replace("0", "۰")
            .replace("1", "۱")
            .replace("2", "۲")
            .replace("3", "۳")
            .replace("4", "۴")
            .replace("5", "۵")
            .replace("6", "۶")
            .replace("7", "۷")
            .replace("8", "۸")
            .replace("9", "۹")
    }
}