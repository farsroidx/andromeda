@file:Suppress("unused")

package andromeda.viewmodel.contract

/**
 * Marker interface for UI state.
 *
 * UI State represents the **single source of truth** for rendering the screen.
 * It must be immutable and fully describe the UI at any given moment.
 *
 * State changes should be driven only by the ViewModel and observed by the UI
 * via StateFlow or similar observable containers.
 */
interface AndromedaUiIntent
