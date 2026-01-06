@file:Suppress("unused", "AssignedValueIsNeverRead")

package ir.farsroidx.andromeda.ui.ktx

import android.view.MotionEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.disableSwipe() {
    addOnItemTouchListener(
        object : RecyclerView.OnItemTouchListener {
            override fun onTouchEvent(
                view: RecyclerView,
                event: MotionEvent,
            ) {}

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

            override fun onInterceptTouchEvent(
                view: RecyclerView,
                event: MotionEvent,
            ): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        this@disableSwipe.parent?.requestDisallowInterceptTouchEvent(true)
                    }
                }

                return false
            }
        },
    )
}

fun RecyclerView.addEndReachListener(
    threshold: Int = 0,
    callback: () -> Unit,
) {
    var hasReachedEnd = false

    var isUserScrolling = false

    val listener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(
                recyclerView: RecyclerView,
                newState: Int,
            ) {
                super.onScrollStateChanged(recyclerView, newState)
                isUserScrolling = newState != RecyclerView.SCROLL_STATE_IDLE
            }

            override fun onScrolled(
                recyclerView: RecyclerView,
                dx: Int,
                dy: Int,
            ) {
                super.onScrolled(recyclerView, dx, dy)

                if (!isUserScrolling) return

                val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return

                val totalItemCount = layoutManager.itemCount

                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                val isAtEnd = lastVisibleItem >= totalItemCount - 1 - threshold

                if (isAtEnd && !hasReachedEnd) {
                    hasReachedEnd = true

                    callback()
                } else if (!isAtEnd) {
                    hasReachedEnd = false
                }
            }
        }

    this.addOnScrollListener(listener)
}
