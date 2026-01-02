@file:Suppress("unused")

package ir.farsroidx.andromeda.logging

import android.util.Log

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

object Logger : AndromedaLogger {

    private var isLoggingEnabled: Boolean = true

    private var defaultTag: String = "Andromeda"

    fun install(isEnabled: Boolean = true, defaultTag: String = "Andromeda") {
        this.isLoggingEnabled = isEnabled
        this.defaultTag       = defaultTag
    }

    override fun log(
        level: AndromedaLogLevel,
        value: Any?,
        tag: String?,
        throwable: Throwable?
    ) {

        if (!isLoggingEnabled) return

        val logTag = tag ?: defaultTag

        val logMessage = (value?.toString() ?: throwable?.message)?.trim()

        if (logMessage.isNullOrBlank()) return

        when (level) {
            AndromedaLogLevel.VERBOSE -> Log.v(logTag, logMessage)
            AndromedaLogLevel.DEBUG   -> Log.d(logTag, logMessage)
            AndromedaLogLevel.INFO    -> Log.i(logTag, logMessage)
            AndromedaLogLevel.WARN    -> Log.w(logTag, logMessage)
            AndromedaLogLevel.ERROR   -> Log.e(logTag, logMessage, throwable)
        }
    }
}