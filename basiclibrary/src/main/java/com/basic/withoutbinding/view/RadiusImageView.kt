package com.basic.withoutbinding.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.annotation.Dimension
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.withSave
import com.basic.withoutbinding.R

/**
 *    @author : Jeffrey
 *    @date   : 2021/6/2-14:16
 *    @desc   :
 *    @version: 1.0
 */
open class RadiusImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {
    var radiusLT = 0
        set(@Dimension value) {
            field = value
            radiusLtRes = field
            refreshRadiusLine()
            postInvalidate()
        }
    var radiusRT = 0
        set(@Dimension value) {
            field = value
            radiusRtRes = field
            refreshRadiusLine()
            invalidate()
        }
    var radiusLB = 0
        set(@Dimension value) {
            field = value
            radiusLbRes = field
            refreshRadiusLine()
            postInvalidate()
        }
    var radiusRB = 0
        set(@Dimension value) {
            field = value
            radiusRbRes = field
            refreshRadiusLine()
            postInvalidate()
        }
    var radius = 0
        set(@Dimension value) {
            field = value
            radiusRes = field
            refreshRadiusLine()
            postInvalidate()
        }
    protected var clipPath: Path = Path()
    private var radiusRes = 0
    private var radiusLtRes = 0
    private var radiusRtRes = 0
    private var radiusLbRes = 0
    private var radiusRbRes = 0

    init {
        context.obtainStyledAttributes(
            attrs, R.styleable.RadiusImageView, 0, 0
        ).apply {
            radiusRes = getLayoutDimension(R.styleable.RadiusImageView_radius, 0)
            radiusLtRes = getLayoutDimension(R.styleable.RadiusImageView_radiusLeftTop, 0)
            radiusRtRes = getLayoutDimension(R.styleable.RadiusImageView_radiusRightTop, 0)
            radiusLbRes = getLayoutDimension(R.styleable.RadiusImageView_radiusLeftBottom, 0)
            radiusRbRes = getLayoutDimension(R.styleable.RadiusImageView_radiusRightBottom, 0)
        }.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val halfSize = width.coerceAtMost(height) / 2
        radius = if (radiusRes == -1) halfSize else radiusRes
        radiusLT = if (radiusLtRes == -1) halfSize else radiusLtRes
        radiusRT = if (radiusRtRes == -1) halfSize else radiusRtRes
        radiusRB = if (radiusRbRes == -1) halfSize else radiusRbRes
        radiusLB = if (radiusLbRes == -1) halfSize else radiusLbRes
        refreshRadiusLine()
    }

    private fun refreshRadiusLine() {
        clipPath.reset()
        val halfSize = width.coerceAtMost(height) / 2
        val radiusLtPul = halfSize.coerceAtMost(if (radiusLT > 0) radiusLT else radius)
        val radiusRtPul = halfSize.coerceAtMost(if (radiusRT > 0) radiusRT else radius)
        val radiusRbPul = halfSize.coerceAtMost(if (radiusRB > 0) radiusRB else radius)
        val radiusLbPul = halfSize.coerceAtMost(if (radiusLB > 0) radiusLB else radius)
        clipPath.moveTo(0f, radiusLtPul.toFloat())
        //        左上轮角
        clipPath.arcTo(RectF(0f, 0f, radiusLtPul * 2f, radiusLtPul * 2f), -180f, 90f)
        //        右上轮角
        clipPath.arcTo(
            RectF(width - radiusRtPul * 2f, 0f, width.toFloat(), radiusRtPul * 2f),
            -90f,
            90f
        )
        //        右下轮角
        clipPath.arcTo(
            RectF(
                width - radiusRbPul * 2f, height - radiusRbPul * 2f,
                width.toFloat(), height.toFloat()
            ), 0f, 90f
        )
        //        左下轮角
        clipPath.arcTo(
            RectF(0f, height - radiusLbPul * 2f, radiusLbPul * 2f, height.toFloat()),
            90f, 90f
        )
        clipPath.close()
    }

    override fun onDraw(canvas: Canvas) {
        val hadRadius =
            radius > 0 || radiusLT > 0 || radiusRT > 0 || radiusRB > 0 || radiusLB > 0
        if (hadRadius) {
            canvas.withSave {
                clipPath(clipPath)
                super.onDraw(canvas)
            }
        } else {
            super.onDraw(canvas)
        }
    }
}