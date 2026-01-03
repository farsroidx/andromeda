@file:Suppress("unused")

package ir.farsroidx.andromeda.ktx

import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

fun Fragment.onBackPressedDispatcher(enabled: Boolean = true, callback: () -> Unit) =
    requireActivity().onBackPressedDispatcher(enabled, callback)

fun ComponentActivity.onBackPressedDispatcher(enabled: Boolean = true, callback: () -> Unit) {
    onBackPressedDispatcher.addCallback(
        owner = this, onBackPressedCallback = object : OnBackPressedCallback(enabled) {
            override fun handleOnBackPressed() {
                callback()
            }
        }
    )
}