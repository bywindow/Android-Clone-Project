package com.example.bmi_calculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val heightEditText: EditText = findViewById(R.id.height_edit_text) // 명시적 타입 선언 방법
        val weightEditText = findViewById<EditText>(R.id.weight_edit_text) // 추론적 타입 선언 방법
        val resultButton = findViewById<Button>(R.id.result_button)

        resultButton.setOnClickListener {

            // 빈 값이 있을 경우 숫자로 변환하지 못해 예외처리가 필요
            if (heightEditText.text.isEmpty() || weightEditText.text.isEmpty()) {
                Toast.makeText(this, "빈 값이 있습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // 중첩된 함수에서 return 할 함수를 명시해줌.
            }
            val height: Int = heightEditText.text.toString().toInt()
            val weight: Int = weightEditText.text.toString().toInt()

            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("height", height)
            intent.putExtra("weight", weight)
            startActivity(intent)
        }
    }
}