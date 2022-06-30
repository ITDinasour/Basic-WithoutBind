package com.basic.withoutbinding

import android.content.Context
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

/**
 *    @author : Jeffrey
 *    @date   : 2021/5/11-19:58
 *    @泛型   : T-初始化init时的数据类型，VB-布局的ViewBinding类型
 *    @version: 1.0
 */
abstract class BasicViewHolderWithoutBinding<T : Any?>(protected val mContext: Context) :
    BasicView, LifecycleObserver {
    abstract fun getItemView(): View

    init {
        mContext.realAction<LifecycleOwner> {  lifecycle.addObserver(this@BasicViewHolderWithoutBinding)}
    }

    open fun initView(data: T) {}

    override fun getContext(): Context {
        return mContext
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected open fun onActivityDestroy() {
    }
}