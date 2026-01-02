@file:Suppress("unused")

package ir.farsroidx.andromeda.foundation.outcome

import kotlin.Throwable as KotlinThrowable

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

/**
 * A structured base exception used to represent domain-level or unexpected errors.
 *
 * This type enhances observability by attaching additional metadata that helps
 * with debugging, logging, and crash reporting. It can be safely passed across
 * data, domain, and presentation layers inside [AndromedaOutcome.Failure].
 *
 * Each error instance includes:
 * - A human-readable [message]
 * - An optional numeric [code]
 * - A unique [errorId] for correlating logs in monitoring systems
 * - A creation [timestamp]
 * - A captured [stack] trace for debugging purposes
 *
 * Extend this class to create meaningful, traceable domain-specific errors.
 */
sealed class AndromedaOutcomeException : AndromedaOutcomeError {

    /**
     * Human-readable description of the error.
     */
    abstract override val message: String

    /**
     * Optional numeric identifier for error type (domain, API, etc.).
     */
    abstract override val code: Int

    /**
     * Unique ID used for log correlation and monitoring.
     */
    val errorId: String = generateErrorId()

    /**
     * Timestamp (in millis since epoch) representing the creation moment.
     */
    val timestamp: Long = System.currentTimeMillis()

    /**
     * Captured stack trace for debugging.
     */
    val stack: String = captureStackTrace()

    override fun toString(): String =
        "AndromedaOutcomeException(errorId=$errorId, message=$message, code=$code, timestamp=$timestamp)"

    /**
     * Represents a general unexpected error when no specific subtype applies.
     */
    data class Unexpected(override val message: String) : AndromedaOutcomeException() {
        override val code: Int = -1
    }

    /**
     * Wraps a Kotlin [Throwable] into a structured exception for cross-layer consistency.
     *
     * @param throwable The original thrown exception.
     */
    data class Throwable(
        val throwable: KotlinThrowable,
        override val message: String = throwable.message ?: "Unhandled error",
    ) : AndromedaOutcomeException() {
        override val code: Int = -1
    }
}

/**
 * Generates a short unique error ID (e.g., "ERR-5F7A13") for correlating logs.
 */
private fun generateErrorId(): String {
    val pool = ('A'..'Z') + ('0'..'9')
    val token = (1..6).joinToString("") { pool.random().toString() }
    return "ERR-$token"
}

/**
 * Captures the current thread's stack trace as a formatted string for debugging.
 */
private fun captureStackTrace(): String =
    Thread.currentThread().stackTrace.joinToString("\n")