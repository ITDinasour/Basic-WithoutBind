package com.basic.withoutbinding
import android.content.Context
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView

/**
 *    @author : Jeffrey
 *    @date   : 2021/5/11-20:01
 *    @泛型   : VB-条目BasicRVViewHolder布局的ViewBinding类型，T-条目数据类型
 *    @version: 1.0
 */
open class BasicRvViewHolderWithoutBinding<T : Any?>(itemView: View) :
    RecyclerView.ViewHolder(itemView), BasicView, LifecycleObserver {
    protected val mContext: Context = itemView.context

    init {
        if (mContext is LifecycleOwner) {
            (mContext as LifecycleOwner).lifecycle.addObserver(this)
        }
        addOnClickListeners(getClickableViews())
    }


    open fun initView(data: T) {}


    open fun updateViewSelected(selected: Boolean) {
        itemView.isSelected = selected
    }


    override fun getContext(): Context {
        return mContext
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected open fun onActivityDestroy() {
    }

    object UpdateViewType {
        const val UpdateViewSelectedState = "BasicViewHolder_UpdateViewSelectedState"
    }
}