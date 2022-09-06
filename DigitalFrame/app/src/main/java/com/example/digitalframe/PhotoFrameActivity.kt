package com.example.digitalframe

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class PhotoFrameActivity : AppCompatActivity() {

    private val photoList = mutableListOf<Uri>()

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
}