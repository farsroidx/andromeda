@file:Suppress("unused")

package ir.farsroidx.andromeda.logging

import java.util.concurrent.locks.ReentrantLock
import kotlin.collections.forEach
import kotlin.concurrent.withLock

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

object CompositeLogger : AndromedaLogger {
    private val loggers = mutableListOf<AndromedaLogger>()

    private val lock = ReentrantLock()

    fun addLoggers(vararg loggers: AndromedaLogger) {
        lock.withLock {
            loggers.forEach { logger ->

                if (!this.loggers.contains(element = logger)) {
                    this.loggers.add(element = logger)
                }
            }
        }
    }

    fun removeLoggers(vararg loggers: AndromedaLogger) {
        lock.withLock {
            loggers.forEach { logger ->

                this.loggers.remove(element = logger)
            }
        }
    }

    override fun log(
        level: AndromedaLogLevel,
        value: Any?,
        tag: String?,
        throwable: Throwable?,
    ) {
        lock.withLock {
            loggers.forEach { logger ->

                logger.log(level, value, tag, throwable)
            }
        }
    }
}
