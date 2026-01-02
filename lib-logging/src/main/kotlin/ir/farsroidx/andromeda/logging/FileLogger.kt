@file:Suppress("unused")

package ir.farsroidx.andromeda.logging

import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

object FileLogger : AndromedaLogger {

    private var isLoggingEnabled: Boolean = true

    private var defaultTag: String = "Andromeda"

    private var logFile: File? = null

    private val lock = ReentrantLock()

    private val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US)

    fun install(
        context: Context,
        isEnabled: Boolean = true,
        defaultTag: String = "Andromeda"
    ) {

        this.isLoggingEnabled = isEnabled
        this.defaultTag       = defaultTag

        val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.US)

        val dir = File(context.getExternalFilesDir(null), "Logs")

        if (!dir.exists()) dir.mkdirs()

        logFile = File(dir, "Log ${dateFormat.format(Date())}.log")

        if (logFile?.exists() == false) logFile?.createNewFile()
    }

    override fun log(level: AndromedaLogLevel, value: Any?, tag: String?, throwable: Throwable?) {

        if (!isLoggingEnabled || logFile == null) return

        val logTag = tag ?: defaultTag

        val message = (value?.toString() ?: throwable?.message)?.trim() ?: return

        val timestamp = dateTimeFormat.format(Date())

        val fullMessage = "$timestamp [$level] $logTag: $message"

        lock.withLock {

            Logger.log(level, message, logTag, throwable)

            try {

                FileWriter(logFile, true).use { writer ->

                    writer.appendLine(fullMessage)

                    throwable?.let { t ->

                        writer.appendLine(Log.getStackTraceString(t))

                    }
                }

            } catch (e: IOException) {
                Logger.e("AndromedaFileLogger", "Failed to write log to file", e)
            }
        }
    }
}