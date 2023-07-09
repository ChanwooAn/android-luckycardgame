package hd.softeer.luckycardgame

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class PlayerCardItemDecorator(private val cardSize: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount ?: 0
        // 카드가 8장일 때 중간에 겹치도록 설정
        val moveSize = cardSize * 0.25

        if (position < itemCount - 1) {
            if (itemCount == 7) {
                outRect.right -= moveSize.toInt()
            } else if (itemCount == 8) {
                outRect.right -= moveSize.toInt() + 4
            }
        }

        if (itemCount < 7) {
            outRect.left += 5
        }
    }
}
