package ir.farsroidx.andromeda.foundation.outcome

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

/**
 * Defines a unified error model used across data, domain, and presentation layers.
 *
 * This interface provides a consistent structure for representing all failure types,
 * such as:
 * - Network issues
 * - API-level errors
 * - Validation or input errors
 * - Domain-specific logic failures
 *
 * Instances of this interface are typically returned inside [AndromedaOutcome.Failure]
 * to propagate controlled errors without relying on exceptions.
 *
 * Implementations should provide at least:
 * - A readable error [message]
 * - An optional numeric [code] identifying the error category or origin
 *
 * Example:
 * ```
 * data class NetworkError(
 *     override val message: String,
 *     override val code: Int = 408
 * ) : OutcomeError
 * ```
 *
 * Usage in a repository:
 * ```
 * return Outcome.Failure(
 *     NetworkError("Timeout while contacting server", 408)
 * )
 * ```
 *
 * Usage in a ViewModel:
 * ```
 * result.onFailure { err ->
 *     uiState = uiState.copy(errorMessage = err.message)
 * }
 * ```
 */
interface AndromedaOutcomeError {
    /**
     * A human-readable description of the error.
     *
     * Typically used for logging, debugging, or mapped further before being
     * displayed in the UI.
     */
    val message: String

    /**
     * Optional numeric identifier for the error type.
     *
     * Useful for:
     * - Mapping backend error codes
     * - Creating automated error-handling rules
     * - Logging and analytics
     *
     * The default value `-1` indicates an unspecified or non-categorized error.
     */
    val code: Int get() = -1
}
