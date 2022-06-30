package com.basic.withoutbinding.view

import android.animation.ObjectAnimator
import android.view.MotionEvent
import android.view.View

/**
 *    @author : Jeffrey
 *    @date   : 2021/8/16-10:51
 *    @desc   :
 *    @version: 1.0
 */
open class ViewScaleHelper(
    val view: View, private val scaleSize: Float = 0.79f, private var scaleTime: Long = 101
) {
    init {
        if (scaleTime > 1000) {
            scaleTime = 1000
        }
        view.setOnTouchListener { view, event ->
            if(view.isEnabled&&view.isFocusable&&view.isClickable){
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (!touched) {
                            touched = true
                            touchScaleXAni.start()
                            touchScaleYAni.start()
                        }
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        if (touched) {
                            touched = false
                            touchScaleXAni.reverse()
                            touchScaleYAni.reverse()
                        }
                    }
                }
            }
            false
        }
        view.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View?) {}
            override fun onViewDetachedFromWindow(v: View?) {
                touchScaleXAni.cancel()
                touchScaleYAni.cancel()
            }
        })
    }

    var touched = false
    private val touchScaleXAni by lazy(LazyThreadSafetyMode.PUBLICATION) {
        ObjectAnimator.ofFloat(view, "scaleX", 1f, scaleSize)
            .apply { duration = scaleTime }
    }
    private val touchScaleYAni by lazy(LazyThreadSafetyMode.PUBLICATION) {
        ObjectAnimator.ofFloat(view, "scaleY", 1f, scaleSize)
            .apply { duration = scaleTime }
    }
}