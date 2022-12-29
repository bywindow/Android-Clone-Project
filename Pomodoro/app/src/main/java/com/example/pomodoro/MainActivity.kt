package com.example.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.SeekBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private val remainMinuteTextView: TextView by lazy {
        findViewById(R.id.remainMinuteText)
    }

    private val remainSecondTextView: TextView by lazy {
        findViewById(R.id.remainSecondText)
    }

    private val minuteSeekBar: SeekBar by lazy {
        findViewById(R.id.minuteSeekBar)
    }

    private var currentCountDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViews()
    }

    private fun bindViews(){
        minuteSeekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        updateRemainTime(progress * 60 * 1000L)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    //카운터 작동 중에 재조작 시 기존 카운터를 멈춰줘야 함
                    currentCountDownTimer?.cancel()
                    currentCountDownTimer = null
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    seekBar ?: return
                    currentCountDownTimer = createCountDownTimer(seekBar.progress * 1000L * 60)
                    currentCountDownTimer?.start()
                }

            }
        )
    }

    private fun createCountDownTimer(initMillis: Long) =
        object : CountDownTimer(initMillis, 1000L){
            override fun onTick(millisUntilFinished: Long) {
                updateRemainTime(millisUntilFinished)
                updateSeekBar(millisUntilFinished)
            }

            override fun onFinish() {
                updateRemainTime(0)
                updateSeekBar(0)
            }

        }

    private fun updateRemainTime(remainMillis: Long) {
        val remainSecond = remainMillis / 1000 // 전체 남은 시간을 초로 나타낸 것

        remainMinuteTextView.text = "%02d".format(remainSecond / 60)
        remainSecondTextView.text = "%02d".format(remainSecond % 60 ) // 60으로 나눈 나머지가 실제 초에 나타낼 수가 됨
    }

    private fun updateSeekBar(remainMillis: Long){
        minuteSeekBar.progress = (remainMillis / 1000 / 60).toInt()
    }
}