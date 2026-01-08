@file:Suppress("unused")

package ir.farsroidx.andromeda.viewmodel.contract

/**
 * Marker interface for all UI actions.
 *
 * UI Actions represent **one-off events** triggered directly by the View layer,
 * such as button clicks, text input changes, or swipe gestures.
 *
 * Actions are sent to the ViewModel and may result in:
 * - State updates
 * - Emitted UI effects
 * - Triggered intents or business logic
 *
 * They should be lightweight and free of business rules.
 */
interface AndromedaUiAction
