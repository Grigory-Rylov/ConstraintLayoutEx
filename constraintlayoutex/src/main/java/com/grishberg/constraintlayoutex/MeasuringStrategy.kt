package com.grishberg.constraintlayoutex

interface MeasuringStrategy {
    fun onMeasure(parent: ConstraintLayoutEx,
                  widthMeasureSpec: Int, heightMeasureSpec: Int)
}