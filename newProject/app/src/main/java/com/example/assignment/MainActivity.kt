package com.example.assignment

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var notificationHelper: NotificationHelper
    private var count: Int = 0

    private val postResultLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted)
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        notificationHelper = NotificationHelper(this)
        setContentView(binding.root)

        if (intent.getBooleanExtra("closeNotification", false)) {
            val activityData = getSharedPreferences("activityData", Context.MODE_PRIVATE)
            count = activityData.getInt("count", 0)

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(NotificationHelper.NOTIFICATION_ID)
        } else {
            count = 0
        }
        binding.countText.text = count.toString()

        if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
            showRationaleDialog(
                "Permission Demo requires Notification Permission",
                "Notification Permission is denied"
            )
        } else {
            postResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        val btnShowToast = binding.toastButton
        btnShowToast.setOnClickListener() {
            Toast.makeText(this, "toast message", Toast.LENGTH_SHORT).show()
        }

        val btnIncrementCount = binding.countButton
        val countText = binding.countText
        btnIncrementCount.setOnClickListener() {
            count++
            countText.text = count.toString()
        }

        val randomData = intent.getIntExtra("randomData", 0)
        if (randomData != 0) {
            count = randomData
            countText.text = count.toString()
        }

        val btnNavigateToRandom = binding.randomButton
        btnNavigateToRandom.setOnClickListener() {
            notificationHelper.showNotification(count)
        }
    }

    private fun showRationaleDialog(title: String, message: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }
}