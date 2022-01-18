package com.basic.withoutbinding

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.appcompat.app.AppCompatActivity

/**
 *    @author : Jeffrey
 *    @date   : 2021/5/13-19:31
 *    @desc   : 基础类
 *    @泛型   : VB-布局的ViewBinding类型
 *    @version: 1.0
 */
abstract class BasicActivityWithoutBinding : AppCompatActivity(), BasicView {

    protected abstract fun initView()
    protected abstract fun initData()
    protected abstract fun initContentView()

    val mHandler by lazy(LazyThreadSafetyMode.PUBLICATION) {
        SafeHandler(this, Handler.Callback { msg: Message -> handleMessage(msg) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initContentView()
        initView()
        initData()
    }


    override fun getContext(): Context {
        return this
    }

    protected open fun handleMessage(msg: Message): Boolean {
        return false
    }

    override fun onBackPressed() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
            finishAfterTransition();
        } else {
            super.onBackPressed();
        }
    }
}

