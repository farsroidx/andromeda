@file:Suppress("unused")

package ir.farsroidx.andromeda.logging

import android.os.SystemClock

/**
 * Logs the current memory usage of the application.
 */
fun printMemoryUsage() {

    val runtime = Runtime.getRuntime()

    val usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024)

    val maxMemory = runtime.maxMemory() / (1024 * 1024)

    val freeMemory = runtime.freeMemory() / (1024 * 1024)

    Logger.d("Max: ${maxMemory}MB, Used: ${usedMemory}MB, Free: ${freeMemory}MB")
}

/**
 * Measures the execution time of a given block of code and logs it.
 */
inline fun benchmark(block: () -> Unit) {
    val start = SystemClock.elapsedRealtime()
    block()
    val end = SystemClock.elapsedRealtime()
    Logger.d("Execution time: ${end - start} ms")
}