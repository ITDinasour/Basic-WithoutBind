package com.basic.withoutbinding

import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import java.io.InputStream

/**
 *    @author : Jeffrey
 *    @date   : 2021/5/11-19:08
 *    @desc   :
 *    @version: 1.0
 */
object BasicUtil {
    private const val TAG = "TAG-BasicUtil"
    fun logI(msg: String) {
        Log.i(TAG, msg)
    }

    fun logE(msg: String? = null, throwable: Throwable? = null) {
        Log.e(TAG, msg ?: "Error : ", throwable)
    }

    fun isServiceRunning(context: Context, servicePackage: String?): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE)
        if (manager is ActivityManager) {
            val runningServices = manager.getRunningServices(Int.MAX_VALUE)
            val findService = runningServices.find { it.service.className == servicePackage }
            return findService != null
        }
        return false
    }

    fun isServiceRunning(context: Context, service: Class<Service>): Boolean {
        return isServiceRunning(
            context,
            service.name
        )
    }

    fun decodeBitmapFromResource(
        res: Resources, resId: Int, reqWidth: Int, reqHeight: Int
    ): Bitmap? {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(res, resId, options)
        val inSampleSize =
            getInSampleSize(options, reqWidth, reqHeight)
        if (inSampleSize > 1) {
            options.inSampleSize = inSampleSize
        }
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeStream(res.openRawResource(+resId), null, options)
    }

    fun getInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    fun decodeBitmapFromResourceByStream(
        res: Resources, resId: Int, reqWidth: Int, reqHeight: Int
    ) = decodeBitmapFromResourceByStream(
        res.openRawResource(+resId), reqWidth, reqHeight
    )

    fun decodeBitmapFromResourceByStream(
        inputStream: InputStream, reqWidth: Int, reqHeight: Int
    ): Bitmap? {
        if (inputStream.markSupported()) {
            runCatching {
                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                BitmapFactory.decodeStream(inputStream, null, options)
                val inSampleSize = getInSampleSize(options, reqWidth, reqHeight)
                if (inSampleSize > 1) {
                    options.inSampleSize = inSampleSize
                }
                options.inPreferredConfig = Bitmap.Config.ARGB_8888
                options.inJustDecodeBounds = false
                inputStream.reset()
                return BitmapFactory.decodeStream(inputStream, null, options)
            }
        }

        return BitmapFactory.decodeStream(inputStream)
    }


    fun drawable2Bitmap(drawable: Drawable): Bitmap {
        //        如果是bitmap则直接转成bitmap
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        //        如果不是bitmap，则画出bitmap
        val config =
            if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.ARGB_4444
        val w = drawable.intrinsicWidth
        val h = drawable.intrinsicHeight
        val bitmap = if (h <= 0 || w <= 0) {
            Bitmap.createBitmap(1, 1, config) // Single color bitmap will be created of 1x1 pixel
        } else {
            Bitmap.createBitmap(w, h, config)
        }
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, w, h)
        drawable.draw(canvas)
        return bitmap
    }

    /**
     * 获取屏幕宽度
     *
     * @return 屏幕宽度
     */
    fun getScreenWidth() = Resources.getSystem().displayMetrics.widthPixels


    /**
     * 获取屏幕高度
     *
     * @return 屏幕高度
     */
    fun getScreenHeight() = Resources.getSystem().displayMetrics.heightPixels

    /**
     *判断是否是第一次安装的用户
     */
    fun isFirstInstallUser(context: Context): Boolean {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        return packageInfo.firstInstallTime == packageInfo.lastUpdateTime
    }

    fun isDebug(context: Context) =
        runCatching {
            (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        }.getOrDefault(false)

    fun getColor(context: Context, @ColorRes colorRes: Int) =
        ContextCompat.getColor(context, colorRes)

    fun getString(context: Context, @StringRes resId: Int) = context.getString(resId)
    fun getDimension(context: Context, @DimenRes dimResId: Int) =
        context.resources.getDimension(dimResId)

    fun getColorStateList(context: Context, @ColorRes colorRes: Int) =
        ContextCompat.getColorStateList(context, colorRes)

    fun getDrawable(context: Context, @DrawableRes drawableRes: Int) =
        ContextCompat.getDrawable(context, drawableRes)

    fun getString(context: Context, @StringRes resId: Int, vararg formatArgs: Any?) =
        context.getString(resId, *formatArgs)
}