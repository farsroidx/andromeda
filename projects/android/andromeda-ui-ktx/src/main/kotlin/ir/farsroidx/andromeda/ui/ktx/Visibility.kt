@file:Suppress("unused")

package ir.farsroidx.andromeda.ui.ktx

import android.view.View

/**
 * Sets the visibility of the view to visible (View.VISIBLE).
 */
fun View.visible() {
    this.visibility = View.VISIBLE
}

/**
 * Sets the visibility of the view to invisible (View.INVISIBLE).
 */
fun View.invisible() {
    this.visibility = View.INVISIBLE
}

/**
 * Sets the visibility of the view to gone (View.GONE).
 */
fun View.gone() {
    this.visibility = View.GONE
}

/**
 * Sets the visibility of the view to either visible or invisible based on the boolean flag `isVisible`.
 *
 * @param isVisible If true, the view becomes visible. If false, the view becomes invisible.
 */
fun View.visibleOrInvisible(isVisible: Boolean) {
    this.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
}

/**
 * Sets the visibility of the view to either visible or gone based on the boolean flag `isVisible`.
 *
 * @param isVisible If true, the view becomes visible. If false, the view becomes gone.
 */
fun View.visibleOrGone(isVisible: Boolean) {
    this.visibility = if (isVisible) View.VISIBLE else View.GONE
}

/**
 * Sets the visibility of the view to either invisible or visible based on the boolean flag `isInvisible`.
 *
 * @param isInvisible If true, the view becomes invisible. If false, the view becomes visible.
 */
fun View.invisibleOrVisible(isInvisible: Boolean) {
    this.visibility = if (isInvisible) View.INVISIBLE else View.VISIBLE
}

/**
 * Sets the visibility of the view to either invisible or gone based on the boolean flag `isInvisible`.
 *
 * @param isInvisible If true, the view becomes invisible. If false, the view becomes gone.
 */
fun View.invisibleOrGone(isInvisible: Boolean) {
    this.visibility = if (isInvisible) View.INVISIBLE else View.GONE
}

/**
 * Sets the visibility of the view to either gone or visible based on the boolean flag `isGone`.
 *
 * @param isGone If true, the view becomes gone. If false, the view becomes visible.
 */
fun View.goneOrVisible(isGone: Boolean) {
    this.visibility = if (isGone) View.GONE else View.VISIBLE
}

/**
 * Sets the visibility of the view to either gone or invisible based on the boolean flag `isGone`.
 *
 * @param isGone If true, the view becomes gone. If false, the view becomes invisible.
 */
fun View.goneOrInvisible(isGone: Boolean) {
    this.visibility = if (isGone) View.GONE else View.INVISIBLE
}
