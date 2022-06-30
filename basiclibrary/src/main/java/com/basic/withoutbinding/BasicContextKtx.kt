package com.basic.withoutbinding

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

/**
 *    @date   : 2022/4/28-10:28
 *    @desc   : 适配AndroidX在Android5.0以下ImageViewCompat等Compat适配View获取的Context为ContextWrapper
 *    @version: 1.0
 */

val Context.activity: Activity get() = realNonNull()

inline fun <reified T : Any> Context.real(): T? =
    when (this) {
        is T -> this
        is ContextWrapper -> {
            baseContext.run { if (this is T) this else null }
        }
        else -> null
    }

inline fun <reified T : Any> Context.realNonNull(): T =
    if (this is T) this
    else (this as ContextWrapper).baseContext as T

inline fun <reified T : Any> Context.realAction(action: T.() -> Unit) {
    this.real<T>()?.apply { action(this) }
}

inline fun <reified T : Any, R : Any> Context.realActionWithRes(action: T.() -> R): R? =
    this.real<T>()?.run { action(this) }