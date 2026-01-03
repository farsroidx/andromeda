@file:Suppress("unused")

package ir.farsroidx.andromeda.ktx

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresPermission
import androidx.fragment.app.Fragment

@RequiresPermission(Manifest.permission.VIBRATE)
fun Fragment.vibrateDevice(milliseconds: Long = 20) = requireContext().vibrateDevice(milliseconds)

@Suppress("DEPRECATION")
@RequiresPermission(Manifest.permission.VIBRATE)
fun Context.vibrateDevice(milliseconds: Long = 20) {

    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

        val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE)
            as VibratorManager

        vibratorManager.defaultVibrator

    } else {

        getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        vibrator.vibrate(
            VibrationEffect.createOneShot(
                milliseconds, VibrationEffect.DEFAULT_AMPLITUDE
            )
        )

    } else {

        vibrator.vibrate(milliseconds)

    }
}