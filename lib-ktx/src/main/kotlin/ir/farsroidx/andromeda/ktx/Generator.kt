@file:Suppress("unused")

package ir.farsroidx.andromeda.ktx

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import androidx.annotation.RequiresPermission
import androidx.fragment.app.Fragment
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.UUID

@SuppressLint("HardwareIds")
@RequiresPermission(Manifest.permission.READ_PHONE_STATE)
fun Fragment.getUniqueDeviceId(): String = requireContext().getUniqueDeviceId()

@SuppressLint("HardwareIds")
@RequiresPermission(Manifest.permission.READ_PHONE_STATE)
fun Context.getUniqueDeviceId(): String = Settings.Secure.getString(
    contentResolver, Settings.Secure.ANDROID_ID
)
