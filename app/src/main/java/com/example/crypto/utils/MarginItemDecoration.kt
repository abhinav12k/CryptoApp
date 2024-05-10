package com.example.crypto.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration(private val spaceSize: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val marginInPx = spaceSize.dpToPx(view.context).toInt()
        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0) {
                top = marginInPx
            }
            left = marginInPx
            right = marginInPx
            bottom = marginInPx
        }
    }
}