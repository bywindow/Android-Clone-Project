package com.example.digitalframe

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val addPhotoButton : Button by lazy {
        findViewById(R.id.addPhotoButton)
    }

    private val startPhotoFrameButton : Button by lazy {
        findViewById(R.id.startPhotoFrameButton)
    }

    private val imageViewList : List<ImageView> by lazy {
        mutableListOf<ImageView>().apply {
            add(findViewById(R.id.firstRowFirstImage))
            add(findViewById(R.id.firstRowSecondImage))
            add(findViewById(R.id.firstRowThirdImage))
            add(findViewById(R.id.secondRowFirstImage))
            add(findViewById(R.id.secondRowSecondImage))
            add(findViewById(R.id.secondRowThirdImage))
        }
    }

    private val imageUriList : MutableList<Uri> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initAddPhotoButton()
    }

    // 권한 요청에 대한 응답을 콜백으로 받아오는 함수
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1000 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한이 부여되었을 경우
                    navigateToPhotos()
                } else {
                    // 권한을 거부하였을 경우
                    Toast.makeText(this, "권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {

            }
        }
    }

    // 사진 선택 후 결과값을 받아온다
    // TODO : Deprecated
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 예외처리 : 사진을 선택하지 않고 취소했을 경우
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            2000 -> {
                val selectedImgUri: Uri? = data?.data
                if (selectedImgUri != null) {
                    if (imageUriList.size == 6) {
                        Toast.makeText(this, "최대 6장까지 등록가능합니다.", Toast.LENGTH_SHORT).show()
                        return
                    }
                    imageUriList.add(selectedImgUri)
                    imageViewList[imageUriList.size - 1].setImageURI(selectedImgUri)
                } else {
                    Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initAddPhotoButton() {
        addPhotoButton.setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // 권한이 승인되어 있는 경우 : 정상적으로 사진 추가 기능 실행
                    navigateToPhotos()
                }
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    // Educated 화면을 보여준 후 권한 요청
                    showPermissionContextPopup()
                }
                else -> {
                    //Permissions : 권한에 필요한 것들을 배열로 받아서 한번에 요청
                    requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
                }
            }
        }
    }

    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("권한 요청")
            .setMessage("전자액자에 사진을 추가하기 위해 권한 요청에 동의해주세요.")
            .setPositiveButton("확인") { _, _ ->
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
            }
            .setNegativeButton("취소") { _, _ -> }
            .create()
            .show()
    }

    // 기기저장소에 저장된 이미지 파일을 불러온다
    private fun navigateToPhotos() {
        // TODO : Deprecated
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 2000)
    }
}