@file:Suppress("unused")

package ir.farsroidx.andromeda.ui.ktx

import android.telephony.PhoneNumberUtils
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

fun EditText.showError(error: String) {
    this.error = error
    this.requestFocus()
}

fun EditText.hideError() {
    this.error = null
    this.requestFocus()
}

fun EditText.readString(): String = this.text.toString().trim()

fun EditText.isEmptyString(): Boolean {
    return readString().isEmpty()
}

fun EditText.isValidPhone(prefix: String? = "09"): Boolean {
    return if (prefix == null) { PhoneNumberUtils.isGlobalPhoneNumber(readString()) } else {
        readString().startsWith(prefix) && PhoneNumberUtils.isGlobalPhoneNumber(readString())
    }
}

fun EditText.addTextChangedListener(
    beforeTextChanged: (textWatcher: TextWatcher, charSequence: CharSequence, start: Int, count: Int, after: Int) -> Unit = { _, _, _, _, _ -> },
    onTextChanged: (textWatcher: TextWatcher, charSequence: CharSequence, start: Int, before: Int, count: Int) -> Unit = { _,_,_,_,_ -> },
    afterTextChanged: (textWatcher: TextWatcher, editable: Editable) -> Unit = { _,_ -> }
) {

    val watcher = object : TextWatcher {

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            beforeTextChanged(this, s, start, count, after)
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            onTextChanged(this, s, start, before, count)
        }

        override fun afterTextChanged(s: Editable) {
            afterTextChanged(this, s)
        }
    }

    this.addTextChangedListener(watcher)
}
