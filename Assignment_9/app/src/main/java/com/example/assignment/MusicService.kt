package com.example.assignment

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.BatteryManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class MusicService : Service() {
    companion object {
        const val CHANNEL_ID = "MUSIC_CHANNEL_1"
        const val ACTION_PLAY = "com.music.PLAY"
        const val ACTION_REPLAY = "com.music.REPLAY"
        const val ACTION_PAUSE = "com.music.PAUSE"
        const val ACTION_STOP = "com.music.STOP"
        const val N_CHANNEL = "Notification_CHANNEL_1"
    }

    private lateinit var broadcastReceiver: BroadcastReceiver
    private var mediaPlayer : MediaPlayer? = null
    private var musicName = ""
    private var isPause = false
    private var battery = 0

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        val filter = IntentFilter().apply {
            addAction(ACTION_PLAY)
            addAction(ACTION_REPLAY)
            addAction(ACTION_PAUSE)
            addAction(ACTION_STOP)
            addAction(Intent.ACTION_BATTERY_CHANGED)
        }
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    ACTION_REPLAY -> replayMusic()
                    ACTION_PAUSE -> pauseMusic()
                    ACTION_STOP -> stopMusic()
                    Intent.ACTION_BATTERY_CHANGED -> {
                        battery = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
                        notificationUpdate()
                    }
                }
            }
        }
        registerReceiver(broadcastReceiver, filter, Context.RECEIVER_EXPORTED)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            if(intent.getParcelableExtra("MusicUri", Uri::class.java) != null &&
                intent.getStringExtra("MusicName") != null) {
                playMusic(intent.getParcelableExtra("MusicUri", Uri::class.java)!!, intent.getStringExtra("MusicName")!!)
            }
            else{
                Log.e("NO_DATA_TAG","수신 데이터 없음")
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    private fun playMusic(uri : Uri, name : String) {
        musicName = name
        isPause = false

        notificationUpdate()

        val attributes = AudioAttributes.Builder().apply {
            setUsage(AudioAttributes.USAGE_MEDIA)
            setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        }.build()

        mediaPlayer?.apply {
            stop()
            release()
        }

        mediaPlayer = MediaPlayer().apply {
            setDataSource(applicationContext, uri)
            setAudioAttributes(attributes)
            prepare()
            seekTo(0)
            start()
        }
    }

    fun replayMusic() {
        if (isPause) {
            mediaPlayer?.apply {
                start()
            }
            isPause = false
        }
        notificationUpdate()
    }

    fun pauseMusic() {
        if (!isPause) {
            mediaPlayer?.apply {
                pause()
            }
            isPause = true
        }
        notificationUpdate()
    }

    fun stopMusic() {
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null
        isPause = false
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun notificationUpdate() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("MusicName", musicName)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_music_note)
            .setContentTitle(musicName)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()

        startForeground(1, notification)
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = N_CHANNEL
            val descriptionText = "Test Notification"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}