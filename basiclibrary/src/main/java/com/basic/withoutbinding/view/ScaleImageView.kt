package com.basic.withoutbinding.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

/**
 *    @author : Jeffrey
 *    @date   : 2022/1/12-18:50
 *    @desc   :
 *    @version: 1.0
 */
open class ScaleImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    init {
        ViewScaleHelper(this)
    }
}