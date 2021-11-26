package com.basic.withoutbinding

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 *    @author : Jeffrey
 *    @date   : 2021/5/11-19:07
 *    @desc   :
 *    @version: 1.0
 */
interface BasicView {

    fun getClickableViews(): Array<out View>? = null

    fun addOnClickListeners(views: List<View>?) {
        views?.forEach { addOnClickListener(it) }
    }

    fun addOnClickListeners(views: Array<out View>?) {
        views?.forEach { addOnClickListener(it) }
    }

    fun addOnClickListener(vararg views: View) {
        views.forEach { addOnClickListener(it) }
    }


    fun addOnClickListener(view: View, onClick: ((View) -> Unit)? = null) {
        val function = onClick ?: (::onClickView)
        view.setOnClickListener(function)
    }

    fun addOnClickListener(onClickView: (View) -> Unit, vararg views: View) {
        views.forEach { addOnClickListener(it, onClickView) }
    }

    fun onClickView(view: View) {}

    fun getContext(): Context

    fun getBasicColor(@ColorRes colorRes: Int) = BasicUtil.getColor(getContext(), colorRes)
    fun getBasicString(@StringRes resId: Int) = BasicUtil.getString(getContext(), resId)
    fun getBasicDimension(@DimenRes dimResId: Int) = BasicUtil.getDimension(getContext(), dimResId)
    fun getBasicColorStateList(@ColorRes colorRes: Int) =
        BasicUtil.getColorStateList(getContext(), colorRes)

    fun getBasicDrawable(@DrawableRes drawableRes: Int) =
        BasicUtil.getDrawable(getContext(), drawableRes)

    fun getBasicString(@StringRes resId: Int, vararg formatArgs: Any?) =
        BasicUtil.getString(getContext(), resId, *formatArgs)

}

/**
 * ktx-从父布局移除自己
 */
fun View.removeFromParent() {
    parent?.let {
        if (it is ViewGroup) {
            it.removeView(this)
        }
    }
}

/**
 * 展示 - VISIBLE
 */
fun View.show(): Boolean {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
        return true
    }
    return false
}

/**
 * 状态监测 - VISIBLE
 */
val View.isVisible: Boolean
    get() = visibility == View.VISIBLE

/**
 * 不展示 - INVISIBLE
 */
fun View.notShow(): Boolean {
    if (visibility != View.INVISIBLE) {
        visibility = View.INVISIBLE
        return true
    }
    return false
}

/**
 * 状态监测 - INVISIBLE
 */
val View.isInvisible: Boolean
    get() = visibility == View.INVISIBLE

/**
 * 隐藏 - GONE
 */
fun View.hide(): Boolean {
    if (visibility != View.GONE) {
        visibility = View.GONE
        return true
    }
    return false
}

/**
 * 状态监测 - INVISIBLE
 */
val View.isGone: Boolean
    get() = visibility == View.GONE

/**
 * 隐藏或展示
 */
fun View.updateShowHide(isShow: Boolean) = if (isShow) show() else hide()

/**
 * 隐藏或展示
 */
fun View.updateShowNotShow(isShow: Boolean) = if (isShow) show() else notShow()
