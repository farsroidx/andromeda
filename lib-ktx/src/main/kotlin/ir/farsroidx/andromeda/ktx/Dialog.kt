@file:Suppress("DEPRECATION", "unused")

package ir.farsroidx.andromeda.ktx

import android.app.ProgressDialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.StyleRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

fun Fragment.progressDialog(
    message: String, title: String? = null, @StyleRes styleResId: Int? = null
) = requireContext().progressDialog(message, title, styleResId)

fun Fragment.alertDialog(
    @StyleRes styleResId: Int?, invoker: AlertDialog.Builder.() -> Unit
) = requireContext().alertDialog(styleResId, invoker)

fun Context.alertDialog(
    @StyleRes styleResId: Int? = null, invoker: AlertDialog.Builder.() -> Unit
): AlertDialog {

    val dialog = if (styleResId == null) {
        AlertDialog.Builder(this)
    } else {
        AlertDialog.Builder(this, styleResId)
    }

    return dialog.apply(invoker).create()
}

fun Context.progressDialog(
    message: String, title: String? = null, @StyleRes styleResId: Int? = null
): ProgressDialog {

    val dialog = if (styleResId == null) {
        ProgressDialog(this)
    } else {
        ProgressDialog(this, styleResId)
    }

    return dialog.apply {
        progress        = 0
        isIndeterminate = true
        setCancelable(false)
        setMessage(message)
        title?.let { setTitle(it) }
    }
}

fun ProgressDialog?.showDialog() {
    if (this?.isShowing == false) this.show()
}

fun ProgressDialog?.dismissDialog() {
    if (this?.isShowing == true) this.dismiss()
}

fun AlertDialog?.showDialog() {
    if (this?.isShowing == false) this.show()
}

fun AlertDialog?.dismissDialog() {
    if (this?.isShowing == true) this.dismiss()
}

@JvmOverloads
fun AlertDialog.showStyledDialog(buttonOrientation: Int = LinearLayout.HORIZONTAL) {

    if (this.isShowing) return

    this.show().apply {

        val positive = getButton(AlertDialog.BUTTON_POSITIVE)
        val negative = getButton(AlertDialog.BUTTON_NEGATIVE)
        val neutral  = getButton(AlertDialog.BUTTON_NEUTRAL)

        (positive.parent as? LinearLayout)?.let { layout ->

            layout.gravity = Gravity.CENTER_HORIZONTAL

            val leftSpacer = layout.getChildAt(1)

            leftSpacer?.visibility = View.GONE

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {

                if (buttonOrientation == LinearLayout.HORIZONTAL) {
                    layout.orientation = LinearLayout.HORIZONTAL
                } else if (buttonOrientation == LinearLayout.VERTICAL) {
                    layout.orientation = LinearLayout.VERTICAL
                }
            }

            layoutParams.weight  = 1F
            layoutParams.gravity = Gravity.CENTER

            positive.layoutParams = layoutParams
            negative.layoutParams = layoutParams
            neutral.layoutParams  = layoutParams
        }
    }
}