package com.grishberg.constraintlayoutex

import android.graphics.Rect
import android.support.constraint.ConstraintWidgetUpdater
import android.view.Gravity
import android.view.View
import android.view.ViewGroup

class OneRowStrategy : MeasuringStrategy {
    private var stretchableViewWidth: Int = 0
    private val tmpChildRect = Rect()
    private var maxMeasuredWidth = 0
    private var maxMeasuredHeight = 0

    override fun onMeasure(parent: ConstraintLayoutEx, widthMeasureSpec: Int, heightMeasureSpec: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.


        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

        val count = parent.childCount
        var stretchableView: View? = null
        var totalChildWidth = 0
        var totalChildMargins = 0
        var childState = 0

        for (childIndex in 0 until count) {

            val child = parent.getChildAt(childIndex)

            if (child.visibility == View.GONE) {
                continue
            }

            val lp = child.layoutParams as (ConstraintLayoutEx.LayoutParams)

            if (lp.stretchable) {
                stretchableView = child
                continue
            }
            parent.measureChildWithMarginsEx(child, widthMeasureSpec, 0, heightMeasureSpec, 0)
            val measuredChildHeightWithMargins = child.measuredHeight + lp.topMargin + lp.bottomMargin
            maxMeasuredHeight = Math.max(maxMeasuredHeight, measuredChildHeightWithMargins)

            totalChildWidth += child.measuredWidth
            totalChildMargins += lp.leftMargin + lp.rightMargin

            childState = View.combineMeasuredStates(childState, child.measuredState)
        }

        updateMaxMeasuredWidthFromMeasureSpec(widthMode, widthSize, parent)

        updateMaxMeasureHeightFromMeasureSpec(heightMode, heightSize)

        if (stretchableView != null) {
            measureStretchableView(stretchableView, totalChildWidth, totalChildMargins,
                    widthMeasureSpec, heightMeasureSpec, heightMode)
            maxMeasuredHeight = Math.max(maxMeasuredHeight, stretchableView.measuredHeight)
        }

        maxMeasuredHeight = Math.max(maxMeasuredHeight, parent.getSuggestedMinimumHeightEx())
        maxMeasuredWidth = Math.max(maxMeasuredWidth, parent.getSuggestedMinimumWidthEx())

        // Report our final dimensions.
        parent.setMeasuredDimensionEx(
                parent.resolveSizeAndState(maxMeasuredWidth, widthMeasureSpec, childState),
                parent.resolveSizeAndState(maxMeasuredHeight, heightMeasureSpec, childState))

        updateChildrenPosition(parent, 0, 0, maxMeasuredWidth, maxMeasuredHeight)
    }

    private fun measureStretchableView(stretchableView: View,
                                       totalChildWidth: Int,
                                       totalChildMargins: Int,
                                       widthMeasureSpec: Int,
                                       heightMeasureSpec: Int,
                                       heightMode: Int) {
        val lp = stretchableView.layoutParams
        stretchableViewWidth = maxMeasuredWidth - (totalChildWidth + totalChildMargins)

        val childWidthMeasureSpec = ViewGroup.getChildMeasureSpec(widthMeasureSpec, 0, stretchableViewWidth)
        val childHeightMeasureSpec = if (heightMode == View.MeasureSpec.UNSPECIFIED) {
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        } else if (lp.height == ViewGroup.LayoutParams.MATCH_PARENT) {
            ViewGroup.getChildMeasureSpec(heightMeasureSpec, 0, maxMeasuredHeight)
        } else {
            ViewGroup.getChildMeasureSpec(heightMeasureSpec, 0, stretchableView.layoutParams.height)
        }

        stretchableView.measure(childWidthMeasureSpec, childHeightMeasureSpec)
    }

    private fun updateMaxMeasureHeightFromMeasureSpec(heightMode: Int, heightSize: Int) {
        when (heightMode) {
            View.MeasureSpec.EXACTLY -> //Must be this size
                maxMeasuredHeight = heightSize
            View.MeasureSpec.AT_MOST -> //Can't be bigger than...
                maxMeasuredHeight = Math.min(maxMeasuredHeight, heightSize)
            View.MeasureSpec.UNSPECIFIED -> {
                //Be whatever you want
            }
        }
    }

    private fun updateMaxMeasuredWidthFromMeasureSpec(widthMode: Int, widthSize: Int, parent: ConstraintLayoutEx) {
        when (widthMode) {
            View.MeasureSpec.EXACTLY -> //Must be this size
                maxMeasuredWidth = widthSize
            View.MeasureSpec.AT_MOST -> //Can't be bigger than...
                maxMeasuredWidth = Math.min(parent.layoutParams.width, widthSize)
            View.MeasureSpec.UNSPECIFIED -> {
                //Be whatever you want
            }
        }
    }

    private fun updateChildrenPosition(parent: ConstraintLayoutEx, left: Int, top: Int, right: Int, bottom: Int) {
        val count = parent.childCount
        var leftPos = parent.paddingLeft
        val parentTop = parent.paddingTop
        val parentBottom = bottom - top - parent.paddingBottom
        var prevChildGone = false

        for (childIndex in 0 until count) {
            val child = parent.getChildAt(childIndex)

            if (child.visibility == View.GONE) {
                prevChildGone = true
                continue
            }

            val lp = child.layoutParams as (ConstraintLayoutEx.LayoutParams)
            calculateChildRectForLayout(child, parentTop, parentBottom, lp, leftPos, prevChildGone)
            prevChildGone = false

            if (lp.stretchable) {
                tmpChildRect.right = tmpChildRect.left + stretchableViewWidth
            }

            leftPos = tmpChildRect.right + lp.rightMargin

            // Place the child.
            ConstraintWidgetUpdater.updateWidget(lp,
                    tmpChildRect.left,
                    tmpChildRect.top,
                    tmpChildRect.right,
                    tmpChildRect.bottom)
        }
    }

    private fun calculateChildRectForLayout(child: View, parentTop: Int, parentBottom: Int,
                                            lp: ConstraintLayoutEx.LayoutParams, leftPos: Int,
                                            prevChildGone: Boolean) {
        val width = child.measuredWidth
        val childHeight = child.measuredHeight

        val leftMargin: Int = if (lp.goneStartMargin > 0) {
            lp.goneStartMargin
        } else {
            lp.leftMargin
        }

        tmpChildRect.top =
                (parentBottom - parentTop - (childHeight + lp.topMargin + lp.bottomMargin)) / 2
        tmpChildRect.bottom = tmpChildRect.top + childHeight

        tmpChildRect.left = leftPos + leftMargin
        tmpChildRect.right = tmpChildRect.left + width
    }
}