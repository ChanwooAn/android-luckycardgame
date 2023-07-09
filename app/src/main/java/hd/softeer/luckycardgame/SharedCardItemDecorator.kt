package hd.softeer.luckycardgame

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SharedCardItemDecorator(private val spanCount: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val space = when (spanCount) {
            5 -> 40
            4 -> 56
            else -> 12
        }
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        outRect.left = space - column * space / spanCount
        outRect.right = (column + 1) * space / spanCount
        if (position >= spanCount) {
            outRect.top = space
        }
    }
}
