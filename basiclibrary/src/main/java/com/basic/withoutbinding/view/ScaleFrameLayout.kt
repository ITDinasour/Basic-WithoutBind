package com.basic.withoutbinding.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 *    @author : Jeffrey
 *    @date   : 2022/1/12-18:50
 *    @desc   :
 *    @version: 1.0
 */
open class ScaleFrameLayout : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    init {
        ViewScaleHelper(this)
    }
}