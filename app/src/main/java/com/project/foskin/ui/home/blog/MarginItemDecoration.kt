package com.project.foskin.ui.home.blog

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration(private val marginTop: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        // Memberikan marginTop 10dp pada setiap item
        outRect.top = marginTop
    }
}
