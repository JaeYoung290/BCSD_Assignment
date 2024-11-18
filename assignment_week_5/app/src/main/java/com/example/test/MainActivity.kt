package com.example.test

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var count = 0
    private lateinit var countTextView: TextView

    private val activityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val randomValue = result.data?.getIntExtra("randomValue", count)
            count = randomValue ?: count
            countTextView.text = count.toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toastButton: Button = findViewById(R.id.toastButton)
        val countButton: Button = findViewById(R.id.countButton)
        val randomButton: Button = findViewById(R.id.randomButton)
        countTextView = findViewById(R.id.countTextView)

        toastButton.setOnClickListener {
            Toast.makeText(this, "Toast 메시지 출력", Toast.LENGTH_SHORT).show()
        }

        countButton.setOnClickListener {
            count++
            countTextView.text = count.toString()
        }

        randomButton.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("count", count)
            activityLauncher.launch(intent)
        }
    }
}