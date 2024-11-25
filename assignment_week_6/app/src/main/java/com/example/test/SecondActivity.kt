package com.example.test

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlin.random.Random

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val count = intent.getIntExtra("count", 0)

        val randomValue = Random.nextInt(0, count + 1)

        val randomTextView: TextView = findViewById(R.id.randomTextView)
        randomTextView.text = randomValue.toString()

        val backButton: Button = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra("randomValue", randomValue)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}