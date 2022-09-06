package com.example.digitalframe

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.concurrent.timer

class PhotoFrameActivity : AppCompatActivity() {

    private val photoList = mutableListOf<Uri>()

    private var currentPosition = 0;

    private var timer: Timer? = null

    private val foregroundImageView : ImageView by lazy {
        findViewById(R.id.foregroundImageView)
    }

    private val backgroundImageView : ImageView by lazy {
        findViewById(R.id.backgroundImageView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_frame)

        getPhotoUri()
    }

    private fun getPhotoUri() {
        val size  = intent.getIntExtra("photoListSize", 0)
        (0..size).forEach { i ->
            intent.getStringExtra("photo$i")?.let {
                // let을 통해 null이 아닐 경우만 실행되도록 함.
                photoList.add(Uri.parse(it))
            }
        }
    }

    private fun startTimer() {
        timer = timer(period = 5 * 1000) {
            // main thread 에서 동작하지 않으므로 따로 설정해준다.
            runOnUiThread {
                val current = currentPosition
                val next = (current + 1) % photoList.size
                backgroundImageView.setImageURI(photoList[current])

                foregroundImageView.alpha = 0f
                foregroundImageView.setImageURI(photoList[next])
                foregroundImageView.animate()
                    .alpha(1.0f)
                    .setDuration(1000)
                    .start()

                currentPosition = next
            }
        }
    }

    override fun onStop() {
        super.onStop()
        timer?.cancel()
    }

    /**
     * 기존 onCreate 내에 있던 startTimer 함수를 onStart 내부로 이동
     * onCreate 내부에 있다면, 액티비티가 시작될 때 한번만 실행됨
     * onStart 내부에 있도록 하여 Stop 후 다시 액티비티로 돌아왔을 때도 실행되도록
     */
    override fun onStart() {
        super.onStart()
        startTimer()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        timer?.purge()
    }
}