@file:Suppress("unused")

package ir.farsroidx.andromeda.foundation.outcome

import android.util.Log
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

/**
 * Returns `true` if this [AndromedaOutcome] is a [AndromedaOutcome.Success].
 *
 * Uses Kotlin Contracts to enable smart-cast after the check.
 */
@OptIn(ExperimentalContracts::class)
fun <T> AndromedaOutcome<T>.isSuccess(): Boolean {
    contract {
        returns() implies (this@isSuccess is AndromedaOutcome.Success<T>)
    }
    return this is AndromedaOutcome.Success<T>
}

/**
 * Returns `true` if this [AndromedaOutcome] is a [AndromedaOutcome.Failure].
 *
 * Uses Kotlin Contracts for smart-cast support.
 */
@OptIn(ExperimentalContracts::class)
fun <T> AndromedaOutcome<T>.isFailure(): Boolean {
    contract {
        returns() implies (this@isFailure is AndromedaOutcome.Failure)
    }
    return this is AndromedaOutcome.Failure
}

/**
 * Invokes [callback] if this result is [AndromedaOutcome.Success].
 *
 * Enables coroutine-friendly chaining:
 * ```kotlin
 * repository.login()
 *     .onSuccess { user  -> ... }
 *     .onFailure { error -> ... }
 * ```
 *
 * @return The original [AndromedaOutcome] instance for fluent chaining.
 */
suspend fun <T> AndromedaOutcome<T>.onSuccess(callback: suspend (T) -> Unit): AndromedaOutcome<T> =
    also {
        if (this is AndromedaOutcome.Success) callback(this.data)
    }

/**
 * Invokes [callback] if this result is [AndromedaOutcome.Failure].
 *
 * Enables coroutine-friendly chaining similar to `onSuccess`.
 *
 * @return The original [AndromedaOutcome] instance for fluent chaining.
 */
suspend fun <T> AndromedaOutcome<T>.onFailure(
    callback: suspend (AndromedaOutcomeError) -> Unit
): AndromedaOutcome<T> = also {
    if (this is AndromedaOutcome.Failure) callback(this.error)
}

/**
 * Converts a [Throwable] into a structured [AndromedaOutcomeException].
 *
 * This is useful when catching unknown or unexpected exceptions
 * inside repositories, data sources, or coroutine scopes:
 *
 * ```kotlin
 * try {
 *     apiCall()
 * } catch (t: Throwable) {
 *     return t.toOutcomeException()
 * }
 * ```
 *
 * Logs the details for debugging and monitoring.
 *
 * @return A [AndromedaOutcomeException.Throwable] wrapping the original exception.
 */
fun Throwable.toAndromedaOutcomeException(): AndromedaOutcomeException =
    AndromedaOutcomeException.Throwable(throwable = this).also {
        Log.e("Andromeda",
            """
            ==================== AndromedaOutcomeException =====================
            code      : ${it.code}
            errorId   : ${it.errorId}
            message   : ${it.message}
            timestamp : ${it.timestamp}
            stack     :
            ${it.stack}
            =====================================================================
            """.trimIndent()
        )
    }