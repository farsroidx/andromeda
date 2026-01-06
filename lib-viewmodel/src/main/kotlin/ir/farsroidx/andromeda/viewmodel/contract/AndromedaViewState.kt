@file:Suppress("unused")

package ir.farsroidx.andromeda.viewmodel.contract

/**
 * Marker interface representing the immutable state of a screen in MVVM.
 *
 * A ViewState is the single source of truth for rendering the UI.
 * It should fully describe what the UI looks like at any given moment.
 *
 * Implementations are expected to be immutable, typically declared
 * as Kotlin data classes.
 *
 * ViewState must NOT contain:
 * - Business logic
 * - One-time actions (navigation, toasts, snack bars)
 * - References to framework or lifecycle-aware types
 *
 * ViewState is owned and mutated only by the ViewModel.
 */
interface AndromedaViewState
