package com.basic.withoutbinding

import android.content.Context
import android.content.SharedPreferences

/**
 *    @author : Jeffrey
 *    @date   : 2021/7/5-17:02
 *    @version: 1.0
 */
object BasicSp {
    const val spName = "BasicSp"
    private val locker = Any()
    private var spInstance: SharedPreferences? = null

    fun get(context: Context, spName: String = this.spName): SharedPreferences {
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

    fun edit(context: Context, spName: String = context.packageName) = get(context, spName).edit()

    fun apply(context: Context, key: String, value: Any) {
        BasicUtil.logI(
            "apply : $value - " +
                    when (value) {
                        is Boolean -> "Boolean"
                        is String -> "String"
                        is Set<*> -> "Set<*>"
                        is Int -> "Int"
                        is Long -> "Long"
                        is Float -> "Float"
                        else -> "else"
                    }
        )
        when (value) {
            is Boolean -> edit(context).putBoolean(key, value)
            is String -> edit(context).putString(key, value)
            is Set<*> -> {
                if (value.firstOrNull() is String) {
                    edit(context).putStringSet(key, value as Set<String>)
                } else throw throw getSpClassNotFoundException(value)
            }
            is Int -> edit(context).putInt(key, value)
            is Long -> edit(context).putLong(key, value)
            is Float -> edit(context).putFloat(key, value)
            else -> throw throw getSpClassNotFoundException(value)
        }.apply()
    }

    fun commit(context: Context, key: String, value: Any) {
        when (value) {
            is Boolean -> edit(context).putBoolean(key, value)
            is String -> edit(context).putString(key, value)
            is Set<*> -> {
                if (value.firstOrNull() is String) {
                    edit(context).putStringSet(key, value as Set<String>)
                } else throw throw getSpClassNotFoundException(value)
            }
            is Int -> edit(context).putInt(key, value)
            is Long -> edit(context).putLong(key, value)
            is Float -> edit(context).putFloat(key, value)
            else -> throw getSpClassNotFoundException(value)
        }.commit()
    }

    fun <T : Any> get(context: Context, key: String, valueDefault: T): T =
        get(context).run {
            when (valueDefault) {
                is Boolean -> getBoolean(key, valueDefault)
                is String -> getString(key, valueDefault)
                is Set<*> -> {
                    if (valueDefault.firstOrNull() is String) {
                        getStringSet(key, valueDefault as Set<String>)
                    } else throw getSpClassNotFoundException(valueDefault)
                }
                is Int -> getInt(key, valueDefault)
                is Long -> getLong(key, valueDefault)
                is Float -> getFloat(key, valueDefault)
                else -> throw getSpClassNotFoundException(valueDefault)
            }
        } as T

    fun getSpClassNotFoundException(value: Any) =
        ClassNotFoundException("The parameter not contain ${value.javaClass.simpleName} can only be of Boolean, String, Set<String>, Int, Long, Float type")
}