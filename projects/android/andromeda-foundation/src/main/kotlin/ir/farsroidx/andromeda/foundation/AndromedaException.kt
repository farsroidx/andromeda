@file:Suppress("unused")

package ir.farsroidx.andromeda.foundation

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

/**
 * A custom exception class used within Andromeda for throwing exceptions with customizable messages and causes.
 * This class extends [RuntimeException] and supports multiple constructors for flexible usage.
 *
 * @property message A detailed description of the exception's cause.
 * @property cause The underlying [Throwable] that caused the exception.
 */
class AndromedaException : RuntimeException {
    /**
     * Default constructor with no message or cause.
     */
    constructor() : super()

    /**
     * Constructor with a specific message to describe the exception.
     *
     * @param message The message providing details about the exception.
     */
    constructor(message: String) : super(message)

    /**
     * Constructor with a specific cause for the exception.
     *
     * @param cause The underlying cause of this exception, wrapped as a [Throwable].
     */
    constructor(cause: Throwable) : super(cause)

    /**
     * Constructor with both a specific message and cause for the exception.
     *
     * @param message The message describing the exception.
     * @param cause The underlying cause of the exception.
     */
    constructor(message: String? = null, cause: Throwable? = null) : super(message, cause)
}
