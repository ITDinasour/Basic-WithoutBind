package com.base.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.basic.withoutbinding.BasicActivityWithoutBinding

class MainActivity : BasicActivityWithoutBinding() {
    override fun initView() {
    }

    override fun initData() {
    }

    override fun initContentView() {
        setContentView(R.layout.activity_main)
    }

}