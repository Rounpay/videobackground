/*
package com.example.myapplication

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

class MovingBorderGlowView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val basePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val borderPath = Path()
    private val segmentPath = Path()
    private val segmentPath2 = Path()
    private val pathMeasure = PathMeasure()
    private val rectF = RectF()

    private var strokeWidth = 6f
    private var baseColor = Color.DKGRAY
    private var highlightColor = Color.YELLOW
    private val durationMs = 6000L

    private var pathLength = 0f
    private var segmentLength = 0f
    private var animFraction = 0f

    private var animator: ValueAnimator? = null

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        basePaint.apply {
            style = Paint.Style.STROKE
            strokeWidth = this@MovingBorderGlowView.strokeWidth
            color = baseColor
        }

        glowPaint.apply {
            style = Paint.Style.STROKE
            strokeWidth = this@MovingBorderGlowView.strokeWidth
            color = highlightColor
            maskFilter = BlurMaskFilter(8f, BlurMaskFilter.Blur.NORMAL)
        }

        setupAnimator()
    }

    private fun setupAnimator() {
        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = durationMs
            interpolator = LinearInterpolator()
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {
                animFraction = it.animatedValue as Float
                invalidate()
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (animator?.isStarted == false) animator?.start()
    }

    override fun onDetachedFromWindow() {
        animator?.cancel()
        super.onDetachedFromWindow()
    }

    fun setBaseColor(color: Int) {
        baseColor = color
        basePaint.color = color
    }

    fun setHighlightColor(color: Int) {
        highlightColor = color
        glowPaint.color = color
    }

    fun setStrokeWidth(width: Float) {
        strokeWidth = width
        basePaint.strokeWidth = width
        glowPaint.strokeWidth = width
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        val half = strokeWidth / 2f
        rectF.set(half, half, w - half, h - half)

        borderPath.reset()
        borderPath.addRect(rectF, Path.Direction.CW)
        pathMeasure.setPath(borderPath, true)

        pathLength = pathMeasure.length
        segmentLength = pathLength / 3f // visible glowing length
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (pathLength == 0f) return

        // steady border
        canvas.drawPath(borderPath, basePaint)

        // clockwise segment
        val start1 = animFraction * pathLength
        val end1 = (start1 + segmentLength) % pathLength

        segmentPath.reset()
        if (end1 > start1) {
            pathMeasure.getSegment(start1, end1, segmentPath, true)
        } else {
            pathMeasure.getSegment(start1, pathLength, segmentPath, true)
            val tmp = Path()
            pathMeasure.getSegment(0f, end1, tmp, true)
            segmentPath.addPath(tmp)
        }

        // anti-clockwise segment (opposite side)
        val start2 = (pathLength - start1) % pathLength
        val end2 = (start2 + segmentLength) % pathLength

        segmentPath2.reset()
        if (end2 > start2) {
            pathMeasure.getSegment(start2, end2, segmentPath2, true)
        } else {
            pathMeasure.getSegment(start2, pathLength, segmentPath2, true)
            val tmp = Path()
            pathMeasure.getSegment(0f, end2, tmp, true)
            segmentPath2.addPath(tmp)
        }

        // draw both glowing borders
        canvas.drawPath(segmentPath, glowPaint)
        canvas.drawPath(segmentPath2, glowPaint)
    }
}
*/
package com.example.myapplication

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator


class MovingBorderGlowView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val basePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val glowPaint1 = Paint(Paint.ANTI_ALIAS_FLAG)
    private val glowPaint2 = Paint(Paint.ANTI_ALIAS_FLAG)
    private val borderPath = Path()
    private val pathMeasure = PathMeasure()
    private val rectF = RectF()

    private var strokeWidth = 10f
    private var baseColor = Color.parseColor("#303030")
    private var glowColor = Color.YELLOW
    private var pathLength = 0f
    private var animFraction = 0f
    private var animator: ValueAnimator? = null

    private val tempPath1 = Path()
    private val tempPath2 = Path()

    // Border segment length and gap ratio
    private val segmentRatio = 0.35f // 35% visible, 30% gap between both

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        basePaint.apply {
            style = Paint.Style.STROKE
            strokeWidth = this@MovingBorderGlowView.strokeWidth
            color = baseColor
        }

        glowPaint1.apply {
            style = Paint.Style.STROKE
            strokeWidth = this@MovingBorderGlowView.strokeWidth + 6f
            color = glowColor
            maskFilter = BlurMaskFilter(15f, BlurMaskFilter.Blur.NORMAL)
            strokeCap = Paint.Cap.ROUND
        }

        glowPaint2.apply {
            style = Paint.Style.STROKE
            strokeWidth = this@MovingBorderGlowView.strokeWidth + 6f
            color = glowColor
            maskFilter = BlurMaskFilter(15f, BlurMaskFilter.Blur.NORMAL)
            strokeCap = Paint.Cap.ROUND
        }

        startAnimation()
    }

    private fun startAnimation() {
        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 4000L
            interpolator = LinearInterpolator()
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {
                animFraction = it.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        val half = strokeWidth / 2f
        rectF.set(half, half, w - half, h - half)
        borderPath.reset()
        borderPath.addRoundRect(rectF, 25f, 25f, Path.Direction.CW)

        pathMeasure.setPath(borderPath, true)
        pathLength = pathMeasure.length
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (pathLength == 0f) return

        // Base steady border
        canvas.drawPath(borderPath, basePaint)

        val visibleLen = pathLength * segmentRatio
        val gap = pathLength * 0.3f
        val moveDist = animFraction * pathLength

        // Clockwise
        tempPath1.reset()
        getPathSegment(moveDist, moveDist + visibleLen, tempPath1)
        canvas.drawPath(tempPath1, glowPaint1)

        // Anti-clockwise (shifted by half path + gap)
        tempPath2.reset()
        val oppositeStart = (pathLength - moveDist - gap) % pathLength
        getPathSegment(oppositeStart, oppositeStart + visibleLen, tempPath2)
        canvas.drawPath(tempPath2, glowPaint2)
    }

    private fun getPathSegment(start: Float, end: Float, outPath: Path) {
        val realStart = start % pathLength
        val realEnd = end % pathLength

        if (realEnd > realStart) {
            pathMeasure.getSegment(realStart, realEnd, outPath, true)
        } else {
            pathMeasure.getSegment(realStart, pathLength, outPath, true)
            val tmp = Path()
            pathMeasure.getSegment(0f, realEnd, tmp, true)
            outPath.addPath(tmp)
        }
    }
}


