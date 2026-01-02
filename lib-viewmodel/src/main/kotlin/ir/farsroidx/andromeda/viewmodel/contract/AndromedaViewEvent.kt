@file:Suppress("unused")

package ir.farsroidx.andromeda.viewmodel.contract

/**
 * Marker interface for one-time UI events in MVVM.
 *
 * ViewEvents represent actions that should be handled only once by the View,
 * such as navigation, showing a toast, or displaying a dialog.
 *
 * Unlike ViewState, ViewEvents must NOT be used to describe UI rendering.
 *
 * Events are emitted by the ViewModel and consumed by the View.
 */
interface AndromedaViewEvent