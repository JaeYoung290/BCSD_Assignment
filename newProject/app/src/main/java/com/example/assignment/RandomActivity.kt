package com.example.assignment

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment.databinding.ActivityRandomBinding

class RandomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRandomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRandomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(NotificationHelper.NOTIFICATION_ID)

        val receivedCount = intent.getIntExtra("countData", 0)
        val randomNumberTextView = binding.countTextRandomView
        val randomNum = (0..receivedCount).random()
        randomNumberTextView.text = randomNum.toString()

        val btnBack = binding.backButtonRandomView
        btnBack.setOnClickListener() {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("randomData", randomNum)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }
    }
}