@file:Suppress("unused")
@file:OptIn(ExperimentalContracts::class)

package ir.farsroidx.andromeda.foundation.outcome

import kotlin.contracts.ExperimentalContracts

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

/**
 * A strongly-typed wrapper representing the outcome of an operation.
 *
 * This type provides a clear and predictable structure for handling success or
 * failure states across data, domain, and presentation layers — without relying
 * on exceptions.
 *
 * Common use cases:
 * - Repository operations (network, database, cache)
 * - Domain-level use-cases that propagate controlled errors
 * - ViewModels that expose stable, well-defined states to the UI
 *
 * Example:
 * ```
 * when (val result = userRepository.login(username, password)) {
 *     is AndromedaOutcome.Success -> renderUser(result.data)
 *     is AndromedaOutcome.Failure -> renderError(result.error)
 * }
 * ```
 *
 * With extension helpers:
 * ```
 * userRepository.login(username, password)
 *     .onSuccess { user -> uiState = uiState.copy(data = user) }
 *     .onFailure { err  -> uiState = uiState.copy(error = err.message) }
 * ```
 *
 * @param T The type returned when the operation succeeds.
 */
sealed interface AndromedaOutcome<out T> {
    /**
     * Represents a successful operation.
     *
     * @param data The value produced by the operation.
     */
    data class Success<out T>(
        val data: T,
    ) : AndromedaOutcome<T>

    /**
     * Represents a failed operation.
     *
     * Contains an [AndromedaOutcomeError] describing the reason for failure.
     */
    data class Failure(
        val error: AndromedaOutcomeError,
    ) : AndromedaOutcome<Nothing>

    /**
     * Returns the success value if present, otherwise `null`.
     */
    fun asSuccess(): T? = (this as? Success<T>)?.data

    /**
     * Returns the error value if this is a failure, otherwise `null`.
     */
    fun asFailure(): AndromedaOutcomeError? = (this as? Failure)?.error
}
