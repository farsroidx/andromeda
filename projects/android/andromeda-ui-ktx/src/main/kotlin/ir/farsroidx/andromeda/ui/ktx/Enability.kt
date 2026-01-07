@file:Suppress("unused")

package ir.farsroidx.andromeda.ui.ktx

import android.view.View

/**
 * Enables the view, making it interactable.
 */
fun View.enable() = enableOrDisable(true)

/**
 * Disables the view, making it non-interactable.
 */
fun View.disable() = disableOrEnable(true)

/**
 * Enables or disables the view based on the boolean flag `isEnable`.
 *
 * @param isEnable If true, the view is enabled. If false, the view is disabled.
 */
fun View.enableOrDisable(isEnable: Boolean) {
    this.isEnabled = isEnable
}

/**
 * Disables or enables the view based on the boolean flag `isDisable`.
 *
 * @param isDisable If true, the view is disabled. If false, the view is enabled.
 */
fun View.disableOrEnable(isDisable: Boolean) {
    this.isEnabled = !isDisable
}
