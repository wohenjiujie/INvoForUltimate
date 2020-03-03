package com.graduationproject.invoforultimate.ui.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

/**
 *Created by INvo
 *on 2020-01-11.
 * 自定义属性Translation View
 */
class RouteSearchView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var isStartDraw = false

    private var greenPointColor = Color.parseColor("#00ff00")
    private var redPointColor = Color.parseColor("#ff0000")
    private var greenAlphaPointColor = Color.parseColor("#6600ff00")
    private var redAlphaPointColor = Color.parseColor("#66ff0000")
    private var grayPointColor = Color.parseColor("#BDBDBD")

    private var greenPaint: Paint = Paint()
    private var redPaint: Paint
    private var greenPaintAlpha: Paint
    private var redPaintAlpha: Paint
    private var grayPaint: Paint

    private var greenCircleRadius: Float = 0.0f
    private var greenAlphaCircleRadius: Float = 0.0f

    private var redStartY = 0.0f

    private var redCircleRadius: Float = 0.0f
    private var redAlphaCircleRadius: Float = 0.0f

    init {
        greenPaint.flags = Paint.ANTI_ALIAS_FLAG
        greenPaint.style = Paint.Style.FILL
        greenPaint.color = greenPointColor
        greenPaint.strokeWidth = 0.0f

        greenPaintAlpha = Paint(greenPaint)
        greenPaintAlpha.color = greenAlphaPointColor

        redPaint = Paint(greenPaint)
        redPaint.color = redPointColor

        redPaintAlpha = Paint(greenPaint)
        redPaintAlpha.color = redAlphaPointColor

        grayPaint = Paint(greenPaint)
        grayPaint.color = grayPointColor
    }

    private var drawCount = 0

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isStartDraw) {
            when (drawCount) {
                1 -> {
                    redStartY = (height / 4).toFloat()
                    canvas.drawCircle((width / 2).toFloat(), (height / 4).toFloat(), greenCircleRadius, greenPaint)
                    canvas.drawCircle(
                            (width / 2).toFloat(),
                            (height / 4).toFloat(),
                            greenAlphaCircleRadius,
                            greenPaintAlpha
                    )
                }

                2 -> {
                    canvas.drawCircle((width / 2).toFloat(), (height / 4).toFloat(), greenCircleRadius, greenPaint)
                    canvas.drawCircle(
                            (width / 2).toFloat(),
                            (height / 4).toFloat(),
                            greenAlphaCircleRadius,
                            greenPaintAlpha
                    )

                    canvas.drawCircle(
                            (width / 2).toFloat(), (height / 4 + height / 8 + height / 16).toFloat(), TypedValue
                            .applyDimension(TypedValue.COMPLEX_UNIT_PX, 5.0f, context.resources.displayMetrics), grayPaint
                    )
                    canvas.drawCircle(
                            (width / 2).toFloat(), (height / 2).toFloat(), TypedValue
                            .applyDimension(TypedValue.COMPLEX_UNIT_PX, 5.0f, context.resources.displayMetrics), grayPaint
                    )
                    canvas.drawCircle(
                            (width / 2).toFloat(), (height / 2 + height / 8 - height / 16).toFloat(), TypedValue
                            .applyDimension(TypedValue.COMPLEX_UNIT_PX, 5.0f, context.resources.displayMetrics), grayPaint
                    )

                    canvas.drawCircle((width / 2).toFloat(), redStartY, redCircleRadius, redPaint)
                    canvas.drawCircle((width / 2).toFloat(), redStartY, redAlphaCircleRadius, redPaintAlpha)
                }
            }
        }
    }

    fun startDraw() {
        isStartDraw = true
        drawCount = 1
        val greenAni = ObjectAnimator.ofFloat(
                this, "greenCircleRadius", greenCircleRadius, TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_PX, 12.0f, context.resources.displayMetrics)
        )
        greenAni.duration = 600
        val greenAlphaAni = ObjectAnimator.ofFloat(
                this, "greenAlphaCircleRadius", greenAlphaCircleRadius, TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_PX, 22.0f, context.resources.displayMetrics), 0.0f
        )
        greenAlphaAni.duration = 1500
        val set = AnimatorSet()
        set.playTogether(greenAni, greenAlphaAni)
        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                startDrawRed()
            }
        })
        set.start()
    }

    private fun startDrawRed() {
        drawCount = 2
        val redYAni = ObjectAnimator.ofFloat(this, "redStartY", redStartY, (height * 3 / 4).toFloat())
        val redAni = ObjectAnimator.ofFloat(
                this, "redCircleRadius", redCircleRadius, TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_PX, 12.0f, context.resources.displayMetrics)
        )
        val redAlphaAni = ObjectAnimator.ofFloat(
                this, "redAlphaCircleRadius", redAlphaCircleRadius, TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_PX, 22.0f, context.resources.displayMetrics), 0.0f
        )
        val set = AnimatorSet()
        val set2 = AnimatorSet()
        set.duration = 800
        redAlphaAni.duration = 1500
        set.playTogether(redYAni, redAni)
        set2.playSequentially(set, redAlphaAni)
        set2.start()
    }

    fun getGreenCircleRadius(): Float {
        return greenCircleRadius
    }

    fun setGreenCircleRadius(greenCircleRadius: Float) {
        this.greenCircleRadius = greenCircleRadius
        postInvalidate()
    }

    fun getGreenAlphaCircleRadius(): Float {
        return greenAlphaCircleRadius
    }

    fun setGreenAlphaCircleRadius(greenAlphaCircleRadius: Float) {
        this.greenAlphaCircleRadius = greenAlphaCircleRadius
        postInvalidate()
    }


    fun getRedCircleRadius(): Float {
        return redCircleRadius
    }

    fun setRedCircleRadius(redCircleRadius: Float) {
        this.redCircleRadius = redCircleRadius
        postInvalidate()
    }

    fun getRedAlphaCircleRadius(): Float {
        return redAlphaCircleRadius
    }

    fun setRedAlphaCircleRadius(redAlphaCircleRadius: Float) {
        this.redAlphaCircleRadius = redAlphaCircleRadius
        postInvalidate()
    }


    fun getRedStartY(): Float {
        return redStartY
    }

    fun setRedStartY(redStartY: Float) {
        this.redStartY = redStartY
        postInvalidate()
    }

}