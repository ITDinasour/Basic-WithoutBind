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
open class ScaleFrameLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): FrameLayout(context, attrs, defStyleAttr) {

    init {
        ViewScaleHelper(this)
    }
}