package com.project.foskin.ui.home.blog

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration(private val marginTopDp: Int) : RecyclerView.ItemDecoration() {

    private val marginTopPx: Int
        get() = (marginTopDp * Resources.getSystem().displayMetrics.density).toInt()

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.top = marginTopPx
    }
}
