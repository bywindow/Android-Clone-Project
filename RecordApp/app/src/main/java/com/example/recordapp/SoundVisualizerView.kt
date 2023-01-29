package com.example.recordapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class SoundVisualizerView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    var onRequestCurrentAmplitude: (() -> Int)? = null // activity에서 함수를 대입한다

    private val amplitudePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.purple_500)
        strokeWidth = LINE_WIDTH
        strokeCap = Paint.Cap.ROUND // 라인의 양끝 모양을 동그랗게 만든다
    }
    private var drawingWidth: Int = 0
    private var drawingHeight: Int = 0
    private var drawingAmplitudes: List<Int> = emptyList()
    private var isReplaying: Boolean = false
    private var replayingPosition: Int = 0

    private val visualizeRepeatAction: Runnable = object : Runnable {
        override fun run() {
            if(!isReplaying) {
                // amplitude를 먼저 가져온 다음에 그린다
                val currentAmplitude = onRequestCurrentAmplitude?.invoke() ?: 0 // activity에서 정의한 블럭 호출하고 내부 값을 반환
                drawingAmplitudes = listOf(currentAmplitude) + drawingAmplitudes
            } else {
                replayingPosition++
            }

            invalidate() // 데이터가 추가되었을 때 onDraw를 다시 호출하고 뷰를 갱신한다

            handler?.postDelayed(this, ACTION_INTERVAL) // 특정 시간이 지난 뒤 다시 실행
        }

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        drawingWidth = w
        drawingHeight = h
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas ?: return

        val centerY = drawingHeight / 2f
        var offsetX = drawingWidth.toFloat()

        drawingAmplitudes
            .let { amplitudes ->
                if(isReplaying) {
                    amplitudes.takeLast(replayingPosition) // takeLast : 가장 뒤에 있는 값부터 순서대로 가져옴
                } else {
                    amplitudes
                }
            }
            .forEach { amplitude ->
            val lineLength = amplitude / MAX_AMPLITUDE * drawingHeight * 0.8f
            offsetX -= LINE_SPACE
            if(offsetX < 0) return@forEach

            // amplitude 그리기
            canvas.drawLine(
                offsetX,
                centerY - lineLength / 2f,
                offsetX,
                centerY + lineLength / 2f,
                amplitudePaint
            )
        }
    }

    fun startVisualizing(isReplaying: Boolean) {
        this.isReplaying = isReplaying
        handler?.post(visualizeRepeatAction)
    }

    fun stopVisualizing() {
        handler?.removeCallbacks(visualizeRepeatAction)
    }

    companion object {
        private const val LINE_WIDTH = 10f
        private const val LINE_SPACE = 15f
        private const val MAX_AMPLITUDE = Short.MAX_VALUE.toFloat() // 32767
        private const val ACTION_INTERVAL = 20L
    }
}