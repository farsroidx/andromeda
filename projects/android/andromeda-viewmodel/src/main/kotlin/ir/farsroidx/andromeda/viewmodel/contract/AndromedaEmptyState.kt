@file:Suppress("unused")

package ir.farsroidx.andromeda.viewmodel.contract

/**
 * A default empty implementation of [AndromedaViewState] and [AndromedaUiState].
 *
 * This state is intended for screens that do not require any UI state,
 * such as splash screens, navigation-only screens, or placeholder ViewModels.
 *
 * Using this object avoids nullable states, enforces a consistent ViewModel
 * contract, and keeps state handling explicit and predictable.
 */
data object AndromedaEmptyState : AndromedaViewState, AndromedaUiState
