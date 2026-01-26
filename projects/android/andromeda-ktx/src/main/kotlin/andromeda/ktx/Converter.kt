@file:Suppress("unused")

package andromeda.ktx

import android.content.Context
import android.content.res.Resources

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Float.px: Float
    get() = (this * Resources.getSystem().displayMetrics.density)

val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Float.dp: Float
    get() = (this / Resources.getSystem().displayMetrics.density)

fun Int.dpToPx(context: Context): Float = (this * context.resources.displayMetrics.density)

fun Float.dpToPx(context: Context): Float = (this * context.resources.displayMetrics.density)

fun Int.pxToDp(context: Context): Float = (this / context.resources.displayMetrics.density)

fun Float.pxToDp(context: Context): Float = (this / context.resources.displayMetrics.density)
