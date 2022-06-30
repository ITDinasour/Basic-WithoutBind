package com.basic.withoutbinding

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

/**
 *    @author : Jeffrey
 *    @date   : 2021/11/17-17:32
 *    @desc   :
 *    @version: 1.0
 */
abstract class BasicFragmentWithoutBinding<A : AppCompatActivity> : Fragment(), BasicView {
    //    布局初始化
    abstract fun initRootView(inflater: LayoutInflater, container: ViewGroup?): View

    //初始化视图组件，不做数据的加载
    protected abstract fun initView()

    //初始化数据加载
    protected abstract fun initData()

    // Activity对象
    protected lateinit var mActivity: A

    // 根布局
    protected var mRootView: View? = null

    // 是否进行过懒加载
    protected var isLazyLoaded = false
        private set

    // Fragment 是否可见
    protected var isFragmentVisible = false

    // 是否是 replace Fragment 的形式
    protected var isReplaceFragment = false

    val mHandler by lazy(LazyThreadSafetyMode.PUBLICATION) {
        SafeHandler(mActivity, Handler.Callback { msg: Message -> handleMessage(msg) })
    }

    /**
     * Android X中通过生命周期onResume等取代了原setUserVisibleHint方法
     */
    override fun onResume() {
        super.onResume()
        isReplaceFragment = true
        isFragmentVisible = true
        mRootView?.apply {
//如果还未进行初始化加载，则进行懒加载
            if (!isLazyLoaded) lazyLoad()
        }
    }

    override fun onPause() {
        super.onPause()
        isFragmentVisible = false
    }

    //懒加载，只有在onresume之后在加载视图
    private fun lazyLoad() {
        if (!isLazyLoaded) {
            isLazyLoaded = true
            initData()
        }
    }

    override fun getContext(): Context {
        return mActivity
    }

    fun useLazyLoad(useLazyLoad: Boolean) {
        if (!useLazyLoad) {
            isLazyLoaded = !useLazyLoad
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as A
    }

    protected open fun handleMessage(msg: Message): Boolean {
        return false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mRootView ?: apply {
            mRootView = initRootView(inflater, container)
        }
//如果存在父布局，应当将view从父布局中移除，重新再添加到fragment的布局中
        //如果存在父布局，应当将view从父布局中移除，重新再添加到fragment的布局中
        mRootView?.removeFromParent()
        initView()
        if (isLazyLoaded) {
            initData()
        }
        return mRootView
    }

    //    试图加载
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //        replaceFragment的时候，fragment是替换，则已经onresume过
        if (!isReplaceFragment) {
            if (isFragmentVisible) {
                lazyLoad()
            }
        } else {
            lazyLoad()
        }
    }
}