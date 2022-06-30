package com.basic.withoutbinding

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Message
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import java.lang.ref.WeakReference

/**
 *    @author : Jeffrey
 *    @date   : 2021/5/10-17:35
 *    @desc   :
 *    @version: 1.0
 */
open class SafeHandler(
    context: Context, callBack: Callback? = null, autoRegisterLife: Boolean = true
) : Handler(context.mainLooper, callBack), LifecycleObserver {
    init {
        if (autoRegisterLife) {
            context.real<LifecycleOwner>()?.lifecycle?.addObserver(this)
        }
    }

    private val mContext: WeakReference<Context?> = WeakReference(context)
    override fun dispatchMessage(msg: Message) {
        mContext.get()?.realAction<Activity> {
            if (isFinishing || isDestroyed) {
                return
            }
        }
        mContext.get() ?: apply { return }
        super.dispatchMessage(msg)
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onActivityDestroy() {
        removeCallbacksAndMessages(null)
    }

}