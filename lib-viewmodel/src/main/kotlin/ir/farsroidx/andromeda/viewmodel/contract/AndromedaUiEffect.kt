@file:Suppress("unused")

package ir.farsroidx.andromeda.viewmodel.contract

/**
 * Marker interface for UI side effects.
 *
 * UI Effects represent **single-consumption events** that should not survive
 * configuration changes or process recreation.
 *
 * Common examples include:
 * - Navigation
 * - Showing a toast or snack bar
 * - Opening a dialog
 *
 * Effects are usually collected via Flow or Channel in the View layer.
 */
interface AndromedaUiEffect