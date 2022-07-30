package com.example.secretdiary

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {

    private val firstNumberPicker: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.first_number_picker)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val secondNumberPicker: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.second_number_picker).apply {
            minValue = 0
            maxValue = 9
        }
    }

    private val thirdNumberPicker: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.third_number_picker).apply {
            minValue = 0
            maxValue = 9
        }
    }

    private val openButton: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.open_button)
    }

    private val changePasswordButton: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.change_password_button)
    }

    private var changePasswordMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firstNumberPicker
        secondNumberPicker
        thirdNumberPicker

        openButton.setOnClickListener {
            if (changePasswordMode) {
                Toast.makeText(this, "비밀번호 변경 중입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val passwordPreference = getSharedPreferences("password", Context.MODE_PRIVATE)
            val passwordFromUser = "${firstNumberPicker.value}${secondNumberPicker.value}${thirdNumberPicker.value}"
            if (passwordPreference.getString("password", "000").equals(passwordFromUser)) {
                Toast.makeText(this, "비밀다이어리로 입장합니다.", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, DiaryActivity::class.java))
            } else {
                showErrorAlertDialog()
            }
        }
        changePasswordButton.setOnClickListener {
            if (changePasswordMode) {
                val passwordPreference = getSharedPreferences("password", Context.MODE_PRIVATE)
                val passwordFromUser = "${firstNumberPicker.value}${secondNumberPicker.value}${thirdNumberPicker.value}"
                passwordPreference.edit(true) {
                    putString("password", passwordFromUser)
                }
                changePasswordMode = false
                changePasswordButton.setBackgroundColor(Color.BLACK)
            } else {
                val passwordPreference = getSharedPreferences("password", Context.MODE_PRIVATE)
                val passwordFromUser = "${firstNumberPicker.value}${secondNumberPicker.value}${thirdNumberPicker.value}"
                if (passwordPreference.getString("password", "000").equals(passwordFromUser)) {
                    changePasswordMode = true
                    Toast.makeText(this, "변경할 패스워드를 입력해주세요.", Toast.LENGTH_SHORT).show()
                    changePasswordButton.setBackgroundColor(Color.RED)
                } else {
                    showErrorAlertDialog()
                }
            }
        }
    }
    private fun showErrorAlertDialog() {
        AlertDialog.Builder(this)
            .setTitle("실패!!")
            .setMessage("비밀번호가 잘못되었습니다.")
            .setPositiveButton("확인") { _, _ -> }
            .create()
            .show()
    }
}