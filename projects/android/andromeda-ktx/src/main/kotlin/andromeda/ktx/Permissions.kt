@file:Suppress("unused")

package andromeda.ktx

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

fun Fragment.isPermissionGranted(permission: String): Boolean = requireContext().isPermissionGranted(permission)

fun Context.isPermissionGranted(permission: String): Boolean =
    ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

fun Fragment.isPermissionDenied(permission: String): Boolean = requireContext().isPermissionDenied(permission)

fun Context.isPermissionDenied(permission: String): Boolean =
    ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED

fun Fragment.requestPermissions(
    requestCode: Int,
    vararg permissions: String,
) = requireActivity().requestPermissions(requestCode, *permissions)

fun Activity.requestPermissions(
    requestCode: Int,
    vararg permissions: String,
) = this.requestPermissions(permissions, requestCode)
