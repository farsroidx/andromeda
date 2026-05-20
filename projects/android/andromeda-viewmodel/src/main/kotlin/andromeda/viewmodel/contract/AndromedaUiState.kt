@file:Suppress("unused")

package andromeda.viewmodel.contract

/**
 * Represents the complete and immutable state of the UI.
 *
 * AndromedaUiState is the single source of truth for rendering a screen.
 * All information required to display the UI must be contained in the AndromedaUiState.
 *
 * Implementations should be immutable (usually data classes) and
 * updated only by the ViewModel.
 *
 * AndromedaUiState must NOT contain:
 * - Business logic
 * - One-time events (navigation, toasts, snack bars)
 * - References to framework types (Context, View, Flow, etc.)
 */
interface AndromedaUiState
