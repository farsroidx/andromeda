@file:Suppress("unused")

package ir.farsroidx.andromeda.ui.ktx

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.LayoutRes

fun Spinner.setSpinnerAdapter(
    @LayoutRes layoutId: Int,
    values: List<String>? = null,
    placeholder: String? = null,
) {
    this.adapter =
        ArrayAdapter(
            context,
            layoutId,
            mutableListOf<String>().apply {
                placeholder?.let { add(it) }
                values?.let { addAll(it) }
            },
        )
    this.setSelection(0)
}

fun Spinner.addOnItemSelectedListener(
    onNothingSelected: (parent: AdapterView<*>) -> Unit = {},
    onItemSelected: (parent: AdapterView<*>, view: View, position: Int, id: Long) -> Unit = { _, _, _, _ -> },
) {
    this.onItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long,
            ) {
                onItemSelected(parent, view, position, id)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                onNothingSelected(parent)
            }
        }
}

/**
 * Retrieves the value of the item at the specified index in the Spinner.
 *
 * @param index The position of the item in the Spinner's list.
 * @return The text value of the item at the specified index. If the index is invalid (less than 0),
 *         an empty string is returned.
 *
 * This function retrieves the child view of the Spinner at the given index, casts it to a
 * `TextView`, and then returns its text value. If the index is invalid (less than 0), an
 * empty string is returned to prevent errors.
 */
fun Spinner.getValueFrom(index: Int): String = if (index < 0) "" else (this.getChildAt(index) as TextView).text.toString()
