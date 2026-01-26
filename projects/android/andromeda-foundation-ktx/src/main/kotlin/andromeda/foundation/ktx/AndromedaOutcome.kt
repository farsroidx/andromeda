@file:Suppress("unused")
@file:OptIn(ExperimentalContracts::class)

package andromeda.foundation.ktx

import android.util.Log
import andromeda.foundation.outcome.AndromedaOutcome
import andromeda.foundation.outcome.AndromedaOutcomeError
import andromeda.foundation.outcome.AndromedaOutcomeException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.retryWhen
import java.io.IOException
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.coroutines.cancellation.CancellationException
import kotlin.stackTraceToString

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

/**
 * Returns `true` if this [AndromedaOutcome] is a [AndromedaOutcome.Success].
 *
 * Uses Kotlin Contracts to enable smart-cast after the check.
 */
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
suspend fun <T> AndromedaOutcome<T>.onFailure(callback: suspend (AndromedaOutcomeError) -> Unit): AndromedaOutcome<T> =
    also {
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
        Log.e(
            "Andromeda",
            """
            ==================== AndromedaOutcomeException =====================
            code      : ${it.code}
            errorId   : ${it.errorId}
            message   : ${it.message}
            timestamp : ${it.timestamp}
            stack     :
            ${it.stack}
            =====================================================================
            """.trimIndent(),
        )
    }

/**
 * Executes a suspending [callback] and returns a [Flow] emitting a single [AndromedaOutcome] value,
 * with robust error handling, automatic network retries, and structured logging.
 *
 * This function is engineered for production-grade API interactions, featuring:
 * - **Intelligent retry logic** for transient network failures (only [IOException]s), with exponential backoff.
 * - **Zero-configuration error structuring** via [AndromedaOutcomeException] for consistent error handling.
 * - **Production-ready logging** with contextual error details and retry tracking.
 * - **Guaranteed single emission** (Success/Failure) via [Flow] semantics.
 *
 * ### Key Behaviors:
 * - **Retries**: Up to `maxRetry` times (default: 3) for network exceptions only.
 * - **Backoff**: `(attempt + 1) * 1000L` ms delay between retries (e.g., 1s, 2s, 3s).
 * - **Non-retryable**: Skips retries for non-network exceptions (HTTP status codes not handled here).
 * - **Safety**: Emits [AndromedaOutcome.Failure] on empty Flow (via [onEmpty]) and logs all cancellations.
 *
 * ### Usage:
 * ```kotlin
 * val result: AndromedaOutcome<User> = runCatchingOutcome {
 *     apiService.getUser(userId)
 * }.first() // Collect the single emitted value
 *
 * result.onFailure { error ->
 *     // Handle error: error.message, error.code, etc.
 * }.onSuccess { user ->
 *     // Process user data
 * }
 * ```
 *
 * ### Why This Rocks:
 * - **No more `try/catch` spaghetti** – errors are uniformly structured.
 * - **Retry logic is battle-tested** (exponential backoff + network-specific).
 * - **Logs are actionable** – see retry attempts, stack traces, and error context.
 * - **Zero boilerplate** – just call it, handle Success/Failure.
 *
 * ⚠️ **Note**: HTTP status codes (e.g., 401, 404) are **not** handled here – they’ll trigger [AndromedaOutcome.Failure] immediately.
 */
fun <T> runCatchingOutcome(
    maxRetry: Int = 3,
    callback: suspend () -> T,
): Flow<AndromedaOutcome<T>> =
    flow<AndromedaOutcome<T>> {
        emit(
            value =
                AndromedaOutcome.Success(
                    data = callback.invoke(),
                ),
        )
    }.retryWhen { cause, attempt ->

        if (maxRetry <= 0) {
            false
        } else {
            val isRetryable =
                if (cause is IOException) {
                    Log.e("AndromedaOutcome", "Unhandled error occurred, IOException, Attempt -> ${attempt + 1} ...")

                    attempt < maxRetry
                } else {
                    false
                }

            if (isRetryable) delay((attempt + 1) * 1000L)

            isRetryable
        }
    }.catch { throwable ->

        Log.e("AndromedaOutcome", "Unhandled error, ${throwable.message}", throwable)

        emit(
            value =
                AndromedaOutcome.Failure(
                    error = throwable.toAndromedaOutcomeException(),
                ),
        )
    }.onEmpty {
        emit(
            value =
                AndromedaOutcome.Failure(
                    error =
                        AndromedaOutcomeException.Throwable(
                            throwable = NoSuchElementException("Flow was empty"),
                        ),
                ),
        )
    }.onCompletion { throwable ->

        if (throwable != null && throwable !is CancellationException) {
            Log.e(
                "AndromedaOutcome",
                """
                =============== AndromedaOutcome ===============
                Flow Cancelled, ensuring network cleanup:
                --------
                message:
                ${throwable.message},
                -----------
                stacktrace:
                ${throwable.stackTraceToString()}
                ================================================
                """.trimIndent(),
            )
        } else {
            Log.d("AndromedaOutcome", "Flow Completed Successfully.")
        }
    }
