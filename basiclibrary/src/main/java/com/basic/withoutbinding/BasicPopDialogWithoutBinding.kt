package com.basic.withoutbinding

import android.app.Activity
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.annotation.CallSuper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

/**
 *    @author : Jeffrey
 *    @date   : 2021/5/12-19:42
 *    @泛型   : A-当前PopDialog的Activity限制类型
 *    @version: 1.0
 */
abstract class BasicPopDialogWithoutBinding<A : Activity>(protected val mActivity: A) :
    BasicView, LifecycleObserver {
    protected var mPopWindow: PopupWindow
    protected abstract fun initContentView(): View

    var onDismissListener: PopupWindow.OnDismissListener? = null

    protected var systemUiVisibility = 0

    init {
        mPopWindow =
            PopupWindow(initContentView(), getLayoutWidth(), getLayoutHeight(), true).apply {
                animationStyle = R.style.ScaleAnimStyle
                setBackgroundDrawable(
                    getBasicDrawable(R.drawable.transparent)
                )
                setOnDismissListener {
                    onPopWindowDismiss()
                    onDismissListener?.onDismiss()
                }
            }
        systemUiVisibility = 0
        if (mActivity is LifecycleOwner) {
            mActivity.lifecycle.addObserver(this)
        }
        addOnClickListeners(getClickableViews())
    }


    open fun show() {
        if (mActivity.isFinishing || mActivity.isDestroyed || mPopWindow.isShowing) {
            return
        }
        setBackgroundAlpha(0.5f)
        val decorView = mActivity.window.decorView
        systemUiVisibility = decorView.systemUiVisibility
        decorView.post {
            mPopWindow.showAtLocation(
                decorView, getShowGravity(),
                getShowLocationX(), getShowLocationY()
            )
        }
    }

    open fun show(
        locationView: View,
        offsetX: Int, offsetY: Int, gravity: Int = Gravity.NO_GRAVITY
    ) {
        if (mActivity.isFinishing || mActivity.isDestroyed || mPopWindow.isShowing) {
            return
        }
        systemUiVisibility = 0
        BasicUtil.logI("offsetX=$offsetX,offsetY=$offsetY")
        locationView.post {
            val location = IntArray(2)
            locationView.getLocationOnScreen(location)
            mPopWindow.showAtLocation(
                locationView, gravity, location[0] + offsetX, location[1] + offsetY
            )
        }
    }

    open fun dismiss() {
        if (mPopWindow.isShowing) {
            mPopWindow.dismiss()
        }
    }

    open fun isShowing() = mPopWindow.isShowing

    protected open fun onPopWindowDismiss() {
        if (systemUiVisibility != 0) {
            mActivity.window.decorView.systemUiVisibility = systemUiVisibility
        }
        setBackgroundAlpha(1f)
    }

    protected open fun setBackgroundAlpha(bgAlpha: Float) {
        val activityWindow = mActivity.window
        val lp = activityWindow.attributes
        //0.0-1.0
        lp.alpha = bgAlpha
        activityWindow.attributes = lp
        activityWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

    protected open fun getLayoutHeight() = WindowManager.LayoutParams.WRAP_CONTENT

    protected open fun getLayoutWidth() = WindowManager.LayoutParams.MATCH_PARENT

    protected open fun getShowGravity() = Gravity.CENTER

    protected open fun getShowLocationX() = 0

    protected open fun getShowLocationY() = 0

    override fun getContext() = mActivity

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    @CallSuper
    protected open fun onActivityDestroy() {
    }
}