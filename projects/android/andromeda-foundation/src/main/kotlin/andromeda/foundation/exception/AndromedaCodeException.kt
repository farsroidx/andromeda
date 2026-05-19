@file:Suppress("unused")

package andromeda.foundation.exception

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

/**
 * A custom runtime exception used across Andromeda to represent
 * application-level errors with a stable error code.
 *
 * This exception is mainly designed to be thrown from the data layer
 * and handled gracefully in upper layers such as Domain or UI.
 *
 * @property code A stable, machine-readable error code
 * (e.g. AUTH_EXPIRED, NETWORK, RSA_DECRYPT_FAILED).
 */
class AndromedaCodeException : RuntimeException {
    /**
     * A stable error code that can be used by the UI layer
     * to decide how to present the error.
     */
    val code: String

    /**
     * Creates an exception with only an error code.
     *
     * @param code The error code representing the failure reason.
     */
    constructor(code: String) : super("Unknown exception") {
        this.code = code
    }

    /**
     * Creates an exception with an error code and a human-readable message.
     *
     * @param code The error code representing the failure reason.
     * @param message A detailed message for logging or debugging purposes.
     */
    constructor(code: String, message: String? = null) : super(message) {
        this.code = code
    }

    /**
     * Creates an exception with an error code and an underlying cause.
     *
     * @param code The error code representing the failure reason.
     * @param cause The original throwable that caused this exception.
     */
    constructor(code: String, cause: Throwable? = null) : super(cause) {
        this.code = code
    }

    /**
     * Creates an exception with an error code, message and underlying cause.
     *
     * @param code The error code representing the failure reason.
     * @param message A detailed message for logging or debugging purposes.
     * @param cause The original throwable that caused this exception.
     */
    constructor(code: String, message: String? = null, cause: Throwable? = null) :
        super(message, cause) {
        this.code = code
    }
}
