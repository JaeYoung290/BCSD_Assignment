package com.example.assignment

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class NotificationHelper(private val context: Context) {
    companion object {
        private const val CHANNEL_ID = "random_channel_id"
        private const val CHANNEL_NAME = "Random Channel"
        private const val CHANNEL_DESCRIPTION = "Generate and send random numbers"
        const val NOTIFICATION_ID = 1
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = CHANNEL_NAME
            val descriptionText = CHANNEL_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(count: Int) {
        val activityData = context.getSharedPreferences("activityData", Context.MODE_PRIVATE)
        activityData.edit().putInt("count", count).apply()

        val randomIntent = Intent(context, RandomActivity::class.java).apply {
            putExtra("countData", count)
        }
        val randomPendingIntent: PendingIntent =
            PendingIntent.getActivity(
                context,
                0,
                randomIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        val closeIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("closeNotification", true)
        }
        val closePendingIntent: PendingIntent =
            PendingIntent.getActivity(
                context,
                1,
                closeIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(context.getString((R.string.notification_content)))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(randomPendingIntent)
            .setAutoCancel(true)
            .addAction(
                R.drawable.ic_launcher_foreground,
                context.getString(R.string.btn_more),
                randomPendingIntent
            )
            .addAction(
                R.drawable.ic_launcher_foreground,
                context.getString(R.string.btn_close),
                closePendingIntent
            )

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }
}
