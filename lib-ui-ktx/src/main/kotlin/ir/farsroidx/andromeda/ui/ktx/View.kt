@file:Suppress("unused")

package ir.farsroidx.andromeda.ui.ktx

import android.view.View

/**
 * Removes the click listener from the view by setting it to null.
 */
fun View.removeClickListener() = this.setOnClickListener(null)

/**
 * Removes the long click listener from the view by setting it to null.
 */
fun View.removeLongClickListener() = this.setOnLongClickListener(null)

