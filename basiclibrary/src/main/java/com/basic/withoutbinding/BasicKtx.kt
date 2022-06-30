package com.basic.withoutbinding

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.text.TextPaint
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.annotation.Px
import androidx.core.graphics.component1
import androidx.core.graphics.component2
import androidx.core.graphics.component3
import androidx.core.graphics.component4

/**
 *    @author : Jeffrey
 *    @date   : 2021/5/11-19:45
 *    @desc   :
 *    @version: 1.0
 */
/**
 * dp数值依据手机系统转px，获取实际像素值
 *
 * @param dpValue dp数值
 * @return px数值
 */
val Float.px
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )

fun Activity.hideNavigationBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        val decorView: View = window.decorView
        val option = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        decorView.systemUiVisibility = option
    }
}

inline fun <reified T : Activity> Activity.startActivity(
    bundle: Bundle? = null,
    options: Bundle? = null
) {
    val intent = Intent(this, T::class.java)
    bundle?.run { intent.putExtras(this) }
    startActivity(intent, options)
}

fun Application.killAppProcess() {
    //注意：不能先杀掉主进程，否则逻辑代码无法继续执行，需先杀掉相关进程最后杀掉主进程
    val mActivityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val mList =
        mActivityManager.runningAppProcesses
    for (runningAppProcessInfo in mList) {
        if (runningAppProcessInfo.pid != Process.myPid()) {
            Process.killProcess(runningAppProcessInfo.pid)
        }
    }
//    可以杀死当前应用活动的进程，这一操作将会把所有该进程内的资源（包括线程全部清理掉）。当然，由于 ActivityManager 时刻监听着进程，一旦发现进程被非正常 Kill，它将会试图去重启这个进程。
//    这就是为什么，有时候当我们试图这样去结束掉应用时，发现 app 会自动重新启动的原因.
    Process.killProcess(Process.myPid())
    //表示是正常退出；
    System.exit(0);
//表示是非正常退出，通常这种退出方式应该放在catch块中
//    System.exit(1);
}

fun TextView.setTextColorHorizontalGradient(colorArray: IntArray) {
    setTextColor(BasicUtil.getColor(context, android.R.color.white))
    post {
        val paint: TextPaint = paint
        val width = paint.measureText(text.toString())
        paint.shader =
            LinearGradient(0f, 0f, width, textSize, colorArray, null, Shader.TileMode.CLAMP)
        invalidate()
    }
}

fun Drawable.toBitmap(
    @Px width: Int = intrinsicWidth,
    @Px height: Int = intrinsicHeight,
    config: Bitmap.Config? = null
): Bitmap {
    if (this is BitmapDrawable) {
        if (config == null || bitmap.config == config) {
            // Fast-path to return original. Bitmap.createScaledBitmap will do this check, but it
            // involves allocation and two jumps into native code so we perform the check ourselves.
            if (width == intrinsicWidth && height == intrinsicHeight) {
                return bitmap
            }
            return Bitmap.createScaledBitmap(bitmap, width, height, true)
        }
    }
    val (oldLeft, oldTop, oldRight, oldBottom) = bounds
    val width = if (width > 0) width else 1
    val height = if (height > 0) height else 1
    val config = config ?: Bitmap.Config.ARGB_8888
    val bitmap = Bitmap.createBitmap(width, height, config)
    setBounds(0, 0, width, height)
    draw(Canvas(bitmap))
    setBounds(oldLeft, oldTop, oldRight, oldBottom)
    return bitmap
}


inline fun <reified T : Any> Any.real(): T? =
    if (this is T) this else null

inline fun <reified T : Any> Any.realAction(action: T.() -> Unit) {
    if (this is T) action(this)
}

inline fun <reified T : Any, R : Any> Any.realActionWithRes(action: T.() -> R): R? =
    if (this is T) action(this) else null
