package com.example.postsapp.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.postsapp.R
import kotlin.math.abs

class LoadingView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var time = 0
    private val loadingImage = ContextCompat.getDrawable(context, R.mipmap.loading)!!

    private val leftRect = Rect()
    private val rightRect = Rect()
    private var speed: Int
    private var horizontalShift: Int
    private var verticalShift: Int

    init {
        context.obtainStyledAttributes(attrs, R.styleable.LoadingView, 0, 0)
            .apply {
                try {
                    speed = getInt(R.styleable.LoadingView_speed, 1)
                    val clockwise = getBoolean(R.styleable.LoadingView_clockwise, true)
                    if (!clockwise) {
                        speed = -speed
                    }
                    horizontalShift = getInt(R.styleable.LoadingView_horizontal_shift, 1)
                    verticalShift = getInt(R.styleable.LoadingView_vertical_shift, 0)
                } finally {
                    recycle()
                }
            }
    }

    private fun rotate(canvas: Canvas, x: Float, y: Float) {
        canvas.rotate(time.toFloat(), x, y)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            val picWidth = loadingImage.intrinsicWidth
            val picHeight = loadingImage.intrinsicHeight
            val diffW = width - picWidth
            val diffH = height - picHeight

            val leftBorder = diffW * horizontalShift / 2
            val topBorder = diffH * verticalShift / 2

            leftRect.set(
                leftBorder,
                topBorder,
                leftBorder + picWidth / 2,
                topBorder + picHeight
            )

            rightRect.set(
                leftBorder + picWidth / 2,
                topBorder,
                leftBorder + picWidth,
                topBorder + picHeight
            )

            loadingImage.setBounds(
                leftBorder,
                topBorder,
                leftBorder + picWidth,
                topBorder + picHeight
            )

            time = (time + speed) % 720
            it.save()

            if (abs(time) < 360) {
                rotate(
                    it,
                    (leftBorder + picWidth / 4).toFloat(),
                    (topBorder + picHeight / 2).toFloat()
                )
            }
            it.clipRect(leftRect)
            loadingImage.draw(it)

            it.restore()
            if (abs(time) >= 360) {
                rotate(
                    it,
                    (leftBorder + 3 * picWidth / 4).toFloat(),
                    (topBorder + picHeight / 2).toFloat()
                )
            }
            it.clipRect(rightRect)
            loadingImage.draw(it)

        }

        invalidate()
    }

    class SavedState(state: Parcelable?, val time: Int, val speed: Int) : BaseSavedState(state)

    override fun onSaveInstanceState(): Parcelable? {
        return SavedState(super.onSaveInstanceState(), time, speed)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val myState = state as SavedState
        super.onRestoreInstanceState(myState.superState)
        time = myState.time
        speed = myState.speed
    }
}