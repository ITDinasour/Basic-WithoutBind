package com.base.demo

import android.widget.TextView
import com.basic.withoutbinding.BasicActivityWithoutBinding
import com.basic.withoutbinding.BasicSp
import com.basic.withoutbinding.BasicUtil

class MainActivity : BasicActivityWithoutBinding() {
    override fun initView() {
    }

    override fun initData() {
        BasicSp.apply(this, "1", true)
        BasicSp.apply(this, "2", "2222")
        BasicSp.apply(this, "3", 3333)
        BasicSp.apply(this, "4", 4444L)
        BasicSp.apply(this, "5", 5555.5555f)
    }

    override fun initContentView() {
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.tv_hello_world).setOnClickListener {
            BasicUtil.logI(BasicSp.get(this, "1",false).toString())
            BasicUtil.logI(BasicSp.get(this, "2","null").toString())
            BasicUtil.logI(BasicSp.get(this, "3",0).toString())
            BasicUtil.logI(BasicSp.get(this, "4",0L).toString())
            BasicUtil.logI(BasicSp.get(this, "5",0f).toString())
        }
    }

}