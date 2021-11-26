package com.basic.withoutbinding

import android.content.Context
import android.content.SharedPreferences

/**
 *    @author : Jeffrey
 *    @date   : 2021/7/5-17:02
 *    @desc   :
 *    @version: 1.0
 */
object BasicSp {
    const val spName = "BasicSp"
    private val locker = Any()
    private var spInstance: SharedPreferences? = null

    fun get(context: Context): SharedPreferences {
        return spInstance ?: run {
            synchronized(locker) {
                spInstance ?: run {
                    val sharedPreferences = context.applicationContext.getSharedPreferences(
                        spName, Context.MODE_PRIVATE
                    )
                    spInstance = sharedPreferences
                    sharedPreferences
                }
            }
        }
    }

    fun edit(context: Context) = get(context).edit()
}