package com.basic.withoutbinding

import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

/**
 *    @author : Jeffrey
 *    @date   : 2021/11/16-10:19
 *    @desc   :
 *    @version: 1.0
 */
abstract class BasicRvAdapter<B : Any?, VH : BasicRvViewHolderWithoutBinding<B>> :
    RecyclerView.Adapter<VH>() {

    protected abstract fun getNewCreateViewHolder(parent: ViewGroup, viewType: Int): VH

    protected var mDataList = ArrayList<B>()

    protected var selectEnable = false

    //    选中一个条目
    var itemSelected: B? = null
        set(value) {
            selectEnable = true;
            val oldPositionSelected = field?.run { findPosition(field) } ?: -1
            field = value
            val newPositionSelected = field?.run { findPosition(field) } ?: -1
            if (checkPositionValid(oldPositionSelected)) {
                updateViewItemSelected(false, oldPositionSelected)
            }
            if (checkPositionValid(newPositionSelected)) {
                updateViewItemSelected(true, newPositionSelected)
            }
        }
    var onClickItemListener: ((dataItem: B, viewHolder: VH) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val viewHolder: VH = getNewCreateViewHolder(parent, viewType)
        viewHolder.itemView.setOnClickListener {
            onClickItemListener?.run {
                val adapterPosition = viewHolder.adapterPosition
                getDataItem(adapterPosition)?.run {
                    invoke(this, viewHolder)
                }
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        getDataItem(position)?.run {
            holder.initView(this)
            if (selectEnable) {
                holder.updateViewSelected(isSelected(this))
            }
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        payloads.firstOrNull()?.also { bundle ->
            if (bundle is Bundle) {
                val filter = bundle.keySet().filter { updateType ->
                    updateViewHolder(holder, position, updateType, bundle)
                }
                if (filter.isNotEmpty()) {
                    return
                }
            }
        }
        super.onBindViewHolder(holder, position, payloads)
    }

    /**
     * @param holder :更新item
     * @param position :更新Item下标
     * @param updateViewTypeKey : 更新类型的key
     * @param bundle : 更新类型的键值对
     */
    @CallSuper
    protected open fun updateViewHolder(
        holder: VH, position: Int, updateViewTypeKey: String, bundle: Bundle
    ): Boolean {
        if (updateViewTypeKey == BasicRvViewHolderWithoutBinding.UpdateViewType.UpdateViewSelectedState) {
            holder.updateViewSelected(isSelected(mDataList[position]))
            return true
        }
        return false
    }

    //https://blog.csdn.net/weixin_38380115/article/details/88887238
    override fun getItemCount() = if (mDataList == null) 0 else mDataList.size

    /**
     * 在列表结尾插入数据
     *
     * @param dataList
     */
    open fun addData(dataList: List<B>) {
        if (dataList.isEmpty()) {
            return
        }
        val startIndex = mDataList.size
        mDataList.addAll(dataList)
        notifyItemRangeInserted(startIndex, mDataList.size)
    }

    /**
     * 在列表结尾插入数据
     *
     * @param data
     */
    open fun addData(data: B) {
        mDataList.add(data)
        notifyItemInserted(mDataList.size - 1)
    }

    /**
     * 将头元素移动到尾部
     */
    open fun moveFirst2Last() {
        moveData(0, itemCount - 1)
    }

    /**
     * 将头元素移动到尾部
     */
    open fun moveLast2First() {
        moveData(itemCount - 1, 0)
    }

    /**
     * 将指定位置元素移动到之指定位置
     *
     * @param fromPosition 移动前位置
     * @param toPosition   移动后位置
     */
    open fun moveData(fromPosition: Int, toPosition: Int) {
        if (checkPositionValid(fromPosition) && checkPositionValid(toPosition) && fromPosition != toPosition) {
            if (fromPosition < toPosition) {
                for (i in fromPosition until toPosition) {
                    Collections.swap(mDataList, i, i + 1)
                }
            } else {
                for (i in fromPosition downTo toPosition + 1) {
                    Collections.swap(mDataList, i, i - 1)
                }
            }
            notifyItemMoved(fromPosition, toPosition)
        }
    }

    /**
     * 在列表中移除指定数据节点
     *
     * @param data
     */
    open fun removeData(data: B): Int {
        return findPosition(data).apply {
            removeData(this)
        }
    }

    /**
     * 在列表中移除指定数据节点
     *
     * @param datas
     */
    open fun removeData(datas: List<B>) {
        datas.forEach {
            removeData(findPosition(it))
        }
    }

    /**
     * 在列表中移除指定节点
     *
     * @param position
     */
    open fun removeData(position: Int) {
        if (checkPositionValid(position)) {
            mDataList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    /**
     * 在列表头部插入数据
     *
     * @param dataList
     */
    open fun addDataHeader(dataList: List<B>) {
        if (dataList.isEmpty()) {
            return
        }
        mDataList.addAll(0, dataList)
        notifyItemRangeInserted(0, dataList.size)
    }

    /**
     * 在列表头部插入数据
     *
     * @param data
     */
    open fun addDataHeader(data: B) {
        mDataList.add(0, data)
        notifyItemInserted(0)
    }

    /**
     * 在指定位置插入数据
     *
     * @param data
     */
    open fun addData(index: Int, data: B) {
        var indexNew = index
        if (indexNew < 0) {
            indexNew = 0
        } else {
            if (indexNew > itemCount) {
                indexNew = itemCount
            }
        }
        mDataList.add(indexNew, data)
        notifyItemInserted(indexNew)
    }

    /**
     * 更新数据
     *
     * @param dataList
     */
    open fun setData(dataList: List<B>) {
        if (dataList.size == mDataList.size && dataList == mDataList) {
            return
        }
        val oldSize = mDataList.size
        mDataList = ArrayList(dataList)
        if (mDataList.isNotEmpty()) {
            if (oldSize > 0) {
                notifyDataSetChanged()
            } else {
                notifyItemRangeInserted(0, mDataList.size)
            }
        } else {
            notifyItemRangeRemoved(0, oldSize)
        }
    }

    /**
     * 更新数据
     *
     * @param dataList
     */
    inline fun <reified T : B> setAnyData(dataList: List<Any>) {
        if (dataList.isEmpty()) {
            notifyItemRangeRemoved(0, itemCount)
        } else if (dataList.first() is T) {
            setData(dataList as List<B>)
        }
    }

    protected open fun updateViewItemSelected(b: Boolean, updatePosition: Int) =
        notifyItemChanged(
            updatePosition,
            BasicRvViewHolderWithoutBinding.UpdateViewType.UpdateViewSelectedState
        )

    fun notifyItemChanged(updatePosition: Int, keyUpdate: String) =
        notifyItemRangeChanged(updatePosition, 1, keyUpdate)

    fun notifyItemRangeChanged(positionStart: Int, itemCount: Int, keyUpdate: String) =
        notifyItemRangeChanged(positionStart, itemCount, bundleOf(Pair(keyUpdate, true)))

    /**
     * 判断一个条目是否被选中
     *
     * @param data
     * @return
     */
    open fun isSelected(data: B) = itemSelected?.run { this == data } ?: false

    /**
     * 获取指定位置的条目数据
     *
     * @param position
     * @return
     */
    open fun getDataItem(position: Int) = if (checkPositionValid(position)) {
        mDataList[position]
    } else null

    open fun checkPositionValid(position: Int) = (position >= 0 && position < mDataList.size)

    open fun updateDataSource(newDataSource: ArrayList<B>) {
        mDataList = newDataSource
        notifyDataSetChanged()
    }

    /**
     * 查找指定条目所在列表位置
     *
     * @param b 指定条目数据
     * @return 条目所在位置 -1 ：未找到该条目
     */
    open fun findPosition(b: B?) = b?.run { mDataList.indexOf(this) } ?: -1

    open fun update(dataItem: B) = set(findPosition(dataItem), dataItem)
    open fun set(position: Int, dataItem: B) {
        if (checkPositionValid(position)) {
            mDataList[position] = dataItem
            notifyItemChanged(position)
        }
    }

}