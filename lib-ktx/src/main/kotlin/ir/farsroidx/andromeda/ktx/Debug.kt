@file:Suppress("unused")

package ir.farsroidx.andromeda.ktx

import android.os.StrictMode
import android.util.Log
import android.view.Choreographer
import javax.security.auth.callback.Callback

/**
 * Initializes and applies strict mode policies for debugging only once.
 * This prevents redundant calls from resetting the policies multiple times.
 */
fun initDebugMode() {

    val vmPolicy = StrictMode.VmPolicy.Builder()
        .detectAll()
        .penaltyLog()
        .build()

    val threadPolicy = StrictMode.ThreadPolicy.Builder()
        .detectAll()
        .penaltyLog()
        .build()

    StrictMode.setVmPolicy(vmPolicy)

    StrictMode.setThreadPolicy(threadPolicy)

    Log.d("AndromedaCore", "Application is running in debug mode...")
}

// Global Choreographer.FrameCallback instance for handling fps monitoring
private var fpsCallback: Choreographer.FrameCallback? = null

/**
 * Starts monitoring the FPS (Frames Per Second) of the application.
 */
fun startFpsMonitoring(callback: (fps: Long) -> Unit) {

    if (fpsCallback != null) return // Prevent multiple instances

    fpsCallback = object : Choreographer.FrameCallback {

        var lastTimeNanos: Long = 0

        var frameCount = 0

        override fun doFrame(frameTimeNanos: Long) {

            if (lastTimeNanos == 0L) {
                lastTimeNanos = frameTimeNanos
            }

            val diff = frameTimeNanos - lastTimeNanos

            frameCount++

            if (diff >= 1_000_000_000) {

                val fps = frameCount * 1_000_000_000 / diff

                callback(fps)

                Log.d("AndromedaCore", "Current FPS: $fps")

                lastTimeNanos = frameTimeNanos

                frameCount = 0
            }

            fpsCallback?.let { Choreographer.getInstance().postFrameCallback(it) }
        }
    }

    fpsCallback?.let { Choreographer.getInstance().postFrameCallback(it) }
}

/**
 * Stops the FPS monitoring by removing the callback from Choreographer.
 */
fun stopFpsMonitoring() {
    fpsCallback?.let { Choreographer.getInstance().removeFrameCallback(it) }
    fpsCallback = null
}