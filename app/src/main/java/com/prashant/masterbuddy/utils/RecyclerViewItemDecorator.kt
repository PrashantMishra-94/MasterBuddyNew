package com.prashant.masterbuddy.utils

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by prashant.mishra on 20/09/18.
 */
class RecyclerViewItemDecorator : RecyclerView.ItemDecoration  {

    private var left = 0
    private var right = 0
    private var top = 0
    private var bottom = 2

    constructor()

    constructor(left: Int, top: Int, right: Int, bottom: Int) {
        this.left = left
        this.top = top
        this.right = right
        this.bottom = bottom
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(left, top, right, bottom)
    }

}