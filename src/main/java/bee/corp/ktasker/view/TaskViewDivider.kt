package bee.corp.ktasker.view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class TaskViewDivider(space: Int) : RecyclerView.ItemDecoration() {
    private var divideSpace: Int = space
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = divideSpace
    }
}