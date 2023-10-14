package com.example.infinite.loop.carousel

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CarouselItemDecoration : RecyclerView.ItemDecoration() {

	private val rightOffset = 20
	private val leftOffset = 20

	fun getHorizontalOffset(): Int {
		return rightOffset + leftOffset
	}

	override fun getItemOffsets(
		outRect: Rect,
		view: View,
		parent: RecyclerView,
		state: RecyclerView.State
	) {
		super.getItemOffsets(outRect, view, parent, state)

		outRect.right = 20
		outRect.left = 20
	}
}