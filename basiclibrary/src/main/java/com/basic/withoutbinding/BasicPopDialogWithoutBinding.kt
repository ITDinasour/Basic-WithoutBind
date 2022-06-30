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
    protected val mPopWindow: PopupWindow
    protected abstract fun initContentView(): View

    var onDismissListener: PopupWindow.OnDismissListener? = null

    protected var systemUiVisibility = 0

    init {
        mPopWindow =
            PopupWindow(initContentView(), getLayoutWidth(), getLayoutHeight(), true).apply {
                animationStyle = R.style.ScaleAnimStyle
                isClippingEnabled = true
                setBackgroundDrawable(getBasicDrawable(R.drawable.transparent))
                setOnDismissListener {
                    onDismiss()
                    onDismissListener?.onDismiss()
                }
            }
        systemUiVisibility = 0
    }


    open fun show() {
        val decorView = mActivity.window.decorView
        systemUiVisibility = decorView.systemUiVisibility
        showPopWindow(decorView, getShowGravity(), getShowLocationX(), getShowLocationY())
    }


    open fun show(
        locationView: View, gravity: Int = Gravity.NO_GRAVITY, offsetX: Int, offsetY: Int
    ) {
        systemUiVisibility = 0
        locationView.post {
            val location = IntArray(2)
            locationView.getLocationOnScreen(location)
            showPopWindow(locationView, gravity, location[0] + offsetX, location[1] + offsetY)
        }
    }

    protected open fun showPopWindow(locationView: View, gravity: Int, x: Int, y: Int) {
        if (mActivity.isFinishing || mActivity.isDestroyed || mPopWindow.isShowing) {
            return
        }
        getShowAlpha().apply {
            if (this != 1F) {
                setBackgroundAlpha(this)
            }
        }
        locationView.post {
            runCatching {
                mPopWindow.showAtLocation(locationView, gravity, x, y)
            }.onSuccess {
                onShow()
            }.onFailure {
                onShowFail()
            }
        }
    }

    protected open fun onShow() {
        mActivity.realAction<LifecycleOwner> {
            lifecycle.removeObserver(this@BasicPopDialogWithoutBinding)
            lifecycle.addObserver(this@BasicPopDialogWithoutBinding)
        }
    }

    open fun isShowing() = mPopWindow.isShowing
    open fun dismiss() {
        if (mPopWindow.isShowing) {
            mPopWindow.dismiss()
        }
    }

    protected open fun onDismiss() {
        if (systemUiVisibility != 0) {
            mActivity.window.decorView.systemUiVisibility = systemUiVisibility
        }
        getShowAlpha().apply {
            if (this != 1F) {
                setBackgroundAlpha(1F)
            }
        }
        mActivity.real<LifecycleOwner>()?.lifecycle?.removeObserver(this)
    }

    protected open fun setBackgroundAlpha(bgAlpha: Float) {
        val bgAlphaSet = bgAlpha.coerceAtMost(1F).coerceAtLeast(0F)
        val activityWindow = mActivity.window
        val lp = activityWindow.attributes
        //0.0-1.0
        lp.alpha = bgAlphaSet
        activityWindow.attributes = lp
        activityWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

    protected open fun getLayoutHeight() = WindowManager.LayoutParams.WRAP_CONTENT
    protected open fun getLayoutWidth() = WindowManager.LayoutParams.MATCH_PARENT
    protected open fun getShowAlpha(): Float = 0.5f
    protected open fun getShowGravity() = Gravity.CENTER
    protected open fun getShowLocationX() = 0
    protected open fun getShowLocationY() = 0
    protected open fun onShowFail() {}
    override fun getContext() = mActivity

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    @CallSuper
    protected open fun onActivityDestroy() {
    }
}