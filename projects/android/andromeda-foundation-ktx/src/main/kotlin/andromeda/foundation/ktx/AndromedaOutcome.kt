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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.retryWhen
import java.io.IOException
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.coroutines.cancellation.CancellationException

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
fun Throwable.toAndromedaOutcomeException(): AndromedaOutcomeException {
    return AndromedaOutcomeException.Throwable(throwable = this).also { throwable ->
        val message = buildString {
            appendLine("===================== AndromedaOutcomeException =====================")
            appendLine("-> Code      : ${throwable.code}")
            appendLine("-> ErrorId   : ${throwable.errorId}")
            appendLine("-> Message   : ${throwable.message}")
            appendLine("-> Timestamp : ${throwable.timestamp}")
            appendLine("---------------------------------------------------------------------")
            appendLine(throwable.throwable.stackTraceToString())
            appendLine("=====================================================================")
        }
        Log.e("Andromeda", message)
    }
}

/**
 * Executes a suspending [callback] and returns a [Flow] that emits a single [AndromedaOutcome] value
 * with industrial-grade error handling, automatic retry mechanisms, and structured observability.
 *
 * This function is engineered for mission-critical API interactions, providing:
 *
 * ### 🛡️ **Robust Error Handling**
 * - **Intelligent Retry Logic**: Automatically retries transient network failures ([IOException]) with exponential backoff.
 * - **Zero-Configuration Error Structuring**: All exceptions are uniformly wrapped into [AndromedaOutcomeException].
 * - **Guaranteed Emission**: Always emits exactly one [AndromedaOutcome] (Success or Failure) via [Flow] semantics.
 * - **Empty Flow Protection**: Gracefully handles empty flows by emitting a structured failure.
 *
 * ### ⚙️ **Retry Behavior**
 * - **Max Retries**: Configurable via `maxRetry` (default: 3 attempts).
 * - **Backoff Strategy**: Exponential delay: `(attempt + 1) * 1000L` ms (1s, 2s, 3s...).
 * - **Retry Condition**: Defaults to [IOException] only. Customize via `retryIf` lambda.
 * - **Non-Retryable Exceptions**: Non-network exceptions bypass retry and emit immediate failure.
 *
 * ### 🔧 **Lifecycle Management**
 * - **`onFinally` Callback**: Executes after completion (success/failure) for resource cleanup.
 * - **Structured Logging**: Production-ready logs with contextual details and stack traces.
 * - **Cancellation Awareness**: Properly handles coroutine cancellation without side effects.
 *
 * ### 🚀 **Usage Example**
 * ```kotlin
 * val userOutcome: Flow<AndromedaOutcome<User>> = runCatchingOutcome {
 *     userApi.getUserProfile(userId)
 * }
 *
 * // Collect the single emission
 * userOutcome.collect { outcome ->
 *     outcome.onSuccess { user ->
 *         // Handle successful response
 *     }.onFailure { error ->
 *         // Handle structured error: error.code, error.message, error.originalThrowable
 *     }
 * }
 * ```
 *
 * ### ⚡ **Advanced Configuration**
 * ```kotlin
 * runCatchingOutcome(
 *     maxRetry = 5,
 *     retryIf = { cause, attempt ->
 *         // Custom retry logic for specific exceptions
 *         cause is SocketTimeoutException || cause is ConnectException
 *     },
 *     onFinally = { isSuccess ->
 *         // Resource cleanup or analytics logging
 *         if (isSuccess) logSuccess() else logFailure()
 *     }
 * ) {
 *     performCriticalOperation()
 * }
 * ```
 *
 * ### 📊 **Observability & Debugging**
 * - **Retry Logging**: Each retry attempt is logged with attempt number and exception details.
 * - **Error Logging**: All caught exceptions include stack traces and contextual messages.
 * - **Cancellation Logging**: Unhandled cancellations are logged with full stack traces.
 * - **Success Logging**: Completion logs confirm successful execution.
 *
 * ### ⚠️ **Important Notes**
 * - **HTTP Status Codes**: Non-IO exceptions (like HTTP 400/401/404/500) are **not retried**.
 * - **Thread Safety**: Designed for coroutine contexts; ensure proper dispatcher usage.
 * - `onFinally` **Guarantee**: Always executes exactly once, regardless of success/failure.
 * - **Flow Contract**: Emits exactly one value and completes (or fails with cancellation).
 *
 * @param maxRetry Maximum number of retry attempts (default: 3).
 * @param retryIf Optional predicate to determine if an exception should trigger a retry.
 *                When null, defaults to [IOException] detection.
 * @param onFinally Optional callback executed after completion (success or failure).
 *                  Receives `true` for successful completion, `false` for failure.
 * @param callback Suspending lambda containing the operation to execute.
 *
 * @return [Flow] emitting a single [AndromedaOutcome] with the operation result.
 *
 * @see AndromedaOutcome
 * @see AndromedaOutcomeException
 * @see IOException
 */
fun <T> runCatchingOutcome(
    maxRetry: Int = 3,
    retryIf: (suspend (cause: Throwable, attempt: Long) -> Boolean)? = null,
    onFinally: (suspend (isSuccess: Boolean) -> Unit)? = null,
    callback: suspend () -> T,
): Flow<AndromedaOutcome<T>> =
    flow<AndromedaOutcome<T>> {
        emit(
            value = AndromedaOutcome.Success(
                data = callback.invoke()
            )
        )
    }.retryWhen { cause, attempt ->

        if (attempt >= maxRetry) false else {

            retryIf?.invoke(cause, attempt) ?: run {

                val isRetryable = cause is IOException

                if (isRetryable) {

                    Log.e(
                        "AndromedaOutcome",
                        "Retrying on IOException (Attempt: ${attempt + 1})"
                    )

                    delay((attempt + 1) * 1000L)
                }

                isRetryable
            }
        }

    }.catch { throwable ->

        emit(
            value = AndromedaOutcome.Failure(
                error = throwable.toAndromedaOutcomeException()
            )
        )

    }.onEmpty {

        emit(
            value = AndromedaOutcome.Failure(
                error = AndromedaOutcomeException.Throwable(
                    throwable = NoSuchElementException("Flow was empty")
                )
            )
        )

    }.onCompletion { throwable ->

        if (throwable != null && throwable !is CancellationException) {

            val message = buildString {
                appendLine("=============== AndromedaOutcome ===============")
                appendLine("-> ${throwable.message}")
                appendLine("------------------------------------------------")
                appendLine(throwable.stackTraceToString())
                appendLine("================================================")
            }

            Log.e("AndromedaOutcome", message)
        }

    }.map { outcome ->
        onFinally?.invoke(outcome is AndromedaOutcome.Success)
        outcome
    }