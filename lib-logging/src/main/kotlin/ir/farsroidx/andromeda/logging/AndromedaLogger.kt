@file:Suppress("unused")

package ir.farsroidx.andromeda.logging

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

interface AndromedaLogger {

    fun log(
        level     : AndromedaLogLevel,
        value     : Any?       = null,
        tag       : String?    = null,
        throwable : Throwable? = null
    )

    fun v(value: Any?) =
        log(level = AndromedaLogLevel.VERBOSE, value = value)

    fun d(value: Any?) =
        log(level = AndromedaLogLevel.DEBUG, value = value)

    fun i(value: Any?) =
        log(level = AndromedaLogLevel.INFO, value = value)

    fun w(value: Any?) =
        log(level = AndromedaLogLevel.WARN, value = value)

    fun e(value: Any?) =
        log(level = AndromedaLogLevel.ERROR, value = value)

    fun e(throwable: Throwable) =
        log(level = AndromedaLogLevel.ERROR, throwable = throwable)

    fun e(value: Any?, throwable: Throwable) =
        log(level = AndromedaLogLevel.ERROR, value = value, throwable = throwable)

    fun v(tag: String, value: Any?) =
        log(level = AndromedaLogLevel.VERBOSE, value = value, tag = tag)

    fun d(tag: String, value: Any?) =
        log(level = AndromedaLogLevel.DEBUG, value = value, tag = tag)

    fun i(tag: String, value: Any?) =
        log(level = AndromedaLogLevel.INFO, value = value, tag = tag)

    fun w(tag: String, value: Any?) =
        log(level = AndromedaLogLevel.WARN, value = value, tag = tag)

    fun e(tag: String, value: Any?) =
        log(level = AndromedaLogLevel.ERROR, value = value, tag = tag)

    fun e(tag: String, throwable: Throwable) =
        log(level = AndromedaLogLevel.ERROR, tag = tag, throwable = throwable)

    fun e(tag: String, value: Any?, throwable: Throwable) =
        log(level = AndromedaLogLevel.ERROR, value = value, tag = tag, throwable = throwable)

}