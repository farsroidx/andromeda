@file:Suppress("unused")

package andromeda.ktx

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import androidx.annotation.RequiresPermission
import androidx.fragment.app.Fragment

@SuppressLint("HardwareIds")
@RequiresPermission(Manifest.permission.READ_PHONE_STATE)
fun Fragment.getUniqueDeviceId(): String = requireContext().getUniqueDeviceId()

@SuppressLint("HardwareIds")
@RequiresPermission(Manifest.permission.READ_PHONE_STATE)
fun Context.getUniqueDeviceId(): String =
    Settings.Secure.getString(
        contentResolver,
        Settings.Secure.ANDROID_ID,
    )
