package com.grishberg.constraintlayoutex

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

class ConstraintLayoutEx @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {
    var strategy: MeasuringStrategy? = null

    fun resetStrategy() {
        strategy = null
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (strategy != null) {
            strategy!!.onMeasure(this, widthMeasureSpec, heightMeasureSpec)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    internal fun measureChildWithMarginsEx(child: View, parentWidthMeasureSpec: Int, widthUsed: Int, parentHeightMeasureSpec: Int, heightUsed: Int) {
        super.measureChildWithMargins(child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed)
    }

    internal fun getSuggestedMinimumHeightEx(): Int {
        return super.getSuggestedMinimumHeight()
    }

    internal fun getSuggestedMinimumWidthEx(): Int {
        return super.getSuggestedMinimumWidth()
    }

    internal fun setMeasuredDimensionEx(measuredWidth: Int, measuredHeight: Int) {
        super.setMeasuredDimension(measuredWidth, measuredHeight)
    }

    internal fun resolveSizeAndState(size: Int, measureSpec: Int, childMeasuredState: Int): Int =
            View.resolveSizeAndState(size, measureSpec, childMeasuredState)


    /**
     * Any layout manager that doesn't scroll will want this.
     */
    override fun shouldDelayChildPressedState(): Boolean {
        return false
    }

    override fun generateLayoutParams(attrs: AttributeSet): ConstraintLayoutEx.LayoutParams {
        return LayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): ConstraintLayoutEx.LayoutParams {
        return LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    override fun generateLayoutParams(p: ViewGroup.LayoutParams): ViewGroup.LayoutParams {
        return ViewGroup.LayoutParams(p)
    }

    override fun checkLayoutParams(p: ViewGroup.LayoutParams): Boolean {
        return p is LayoutParams
    }

    class LayoutParams : ConstraintLayout.LayoutParams {
        var stretchable: Boolean = false

        constructor(c: Context, attrs: AttributeSet) : super(c, attrs) {

            // Pull the layout param values from the layout XML during
            // inflation.  This is not needed if you don't care about
            // changing the layout behavior in XML.
            val typedArray = c.obtainStyledAttributes(attrs, R.styleable.ConstraintLayoutEx)
            try {
                stretchable = typedArray.getBoolean(R.styleable.ConstraintLayoutEx_stretchable, false)
            } finally {
                typedArray.recycle()
            }
        }

        constructor(width: Int, height: Int) : super(width, height) {}

        constructor(source: ViewGroup.LayoutParams) : super(source) {}
    }

}