package com.example.assignment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var lapAdapter: LapAdapter
    private val lapTimes = mutableListOf<String>()
    private lateinit var binding: ActivityMainBinding

    private lateinit var handler: Handler
    private var isRunning = false
    private var startTime = 0L
    private var pausedTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lapAdapter = LapAdapter(lapTimes)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = lapAdapter

        handler = Handler(Looper.getMainLooper())

        binding.startPauseButton.setOnClickListener { toggleTimer() }
        binding.stopButton.setOnClickListener { stopTimer()}
        binding.lapButton.setOnClickListener { recordLap() }
    }

    private val updateTimer = object : Runnable {
        override fun run() {
            val currentTime = SystemClock.elapsedRealtime()
            pausedTime = currentTime - startTime
            updateTimerText()
            handler.postDelayed(this, 10)
        }
    }

    private fun toggleTimer() {
        val startPauseButton = binding.startPauseButton
        if (!isRunning) {
            startTime = SystemClock.elapsedRealtime() - pausedTime
            handler.post(updateTimer)
            startPauseButton.text = getString(R.string.pause)
        } else {
            handler.removeCallbacks(updateTimer)
            startPauseButton.text = getString(R.string.start)
        }
        isRunning = !isRunning
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun stopTimer() {
        handler.removeCallbacks(updateTimer)
        pausedTime = 0L
        isRunning = false
        binding.startPauseButton.text = getString(R.string.start)
        lapTimes.clear()
        lapAdapter.notifyDataSetChanged()
        updateTimerText()
    }

    private fun recordLap() {
        if (isRunning) {
            lapTimes.add(0, formatTime(pausedTime))
            lapAdapter.notifyItemInserted(0)
            binding.recyclerView.scrollToPosition(0)
        }
    }

    private fun updateTimerText() {
        binding.timerTextView.text = formatTime(pausedTime)
    }

    private fun formatTime(millis: Long): String {
        val ms = (millis % 1000) / 10
        val seconds = (millis / 1000) % 60
        val minutes = (millis / (1000 * 60)) % 60
        return "%02d : %02d : %02d".format(minutes, seconds, ms)
    }

    override fun onDestroy() {
        handler.removeCallbacks(updateTimer)
        super.onDestroy()
    }
}