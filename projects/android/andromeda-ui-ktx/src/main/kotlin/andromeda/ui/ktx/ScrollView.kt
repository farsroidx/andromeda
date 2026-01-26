@file:Suppress("unused")

package andromeda.ui.ktx

import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun NestedScrollView.addEndReachListener(
    recyclerView: RecyclerView? = null,
    callback: () -> Unit,
) {
    var hasReachedEnd = false

    this.viewTreeObserver.addOnScrollChangedListener {
        val view = getChildAt(childCount - 1)

        val diff = view.bottom - (height + scrollY)

        val isAtEnd = diff <= 0

        if (isAtEnd && !hasReachedEnd) {
            if (recyclerView != null) {
                val layoutManager =
                    recyclerView.layoutManager as? LinearLayoutManager
                        ?: return@addOnScrollChangedListener

                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                if (lastVisibleItem >= totalItemCount - 1) {
                    hasReachedEnd = true
                    callback()
                }
            } else {
                hasReachedEnd = true
                callback()
            }
        } else if (!isAtEnd) {
            hasReachedEnd = false
        }
    }
}
