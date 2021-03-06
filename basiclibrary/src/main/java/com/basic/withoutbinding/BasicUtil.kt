package com.basic.withoutbinding

import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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

    fun getInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int) =
        getInSampleSize(options.outWidth, options.outHeight, reqWidth, reqHeight)

    fun getInSampleSize(sourceWidth: Int, sourceHeight: Int, reqWidth: Int, reqHeight: Int): Int {
        var inSampleSize = 1
        if (sourceHeight > reqHeight || sourceWidth > reqWidth) {
            val halfHeight = sourceHeight / 2
            val halfWidth = sourceWidth / 2
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


    fun drawable2Bitmap(drawable: Drawable): Bitmap = drawable.toBitmap()

    /**
     * ??????????????????
     *
     * @return ????????????
     */
    fun getScreenWidth() = Resources.getSystem().displayMetrics.widthPixels


    /**
     * ??????????????????
     *
     * @return ????????????
     */
    fun getScreenHeight() = Resources.getSystem().displayMetrics.heightPixels

    /**
     *???????????????????????????????????????
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