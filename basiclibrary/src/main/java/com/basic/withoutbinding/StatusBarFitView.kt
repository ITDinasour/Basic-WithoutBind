package com.basic.withoutbinding

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

/**
 *    @author : Jeffrey
 *    @date   : 2021/5/11-19:04
 *    @desc   :
 *    @version: 1.0
 */
class StatusBarFitView : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

init {
    if (visibility == VISIBLE) {
        visibility = INVISIBLE
    }
}

    /**
     * Draw nothing.
     *
     * @param canvas an unused parameter.
     */
    override fun draw(canvas: Canvas?) {}
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        setMeasuredDimension(
            resolveSize(0, widthMeasureSpec),
            resolveSize(
                resources.getDimensionPixelOffset(resourceId),
                heightMeasureSpec
            )
        )
    }
}