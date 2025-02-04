package com.example.assignment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var musicListAdapter: MusicListAdapter
    private lateinit var musicListRecyclerView: RecyclerView
    private var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestPermissions()

        if (hasPermissions(permissionList)) {
            viewMusicList()
        }

        binding.playPauseButton.setOnClickListener { togglePlayPause() }
    }

    private val permissionList = arrayOf(
        Manifest.permission.READ_MEDIA_AUDIO,
        Manifest.permission.POST_NOTIFICATIONS,
        Manifest.permission.FOREGROUND_SERVICE,
        Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK
    )

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val allGranted = permissions.filter { !it.value }.keys

        if (allGranted.isEmpty()) {
            Toast.makeText(this, "모든 권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
        }
        else {
            val deniedMessage = "권한이 거부되었습니다: ${allGranted.joinToString(", ")}"
            Toast.makeText(this, deniedMessage, Toast.LENGTH_LONG).show()
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun hasPermission(permission: String): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun hasPermissions(permissions: Array<String>): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        return permissions.all { hasPermission(it) }
    }

    private fun requestPermissions() {
        if (!hasPermissions(permissionList)) {
            permissionLauncher.launch(permissionList)
        }
    }

    private fun togglePlayPause() {
        if (isPlaying) {
            sendBroadcast(Intent(MusicService.ACTION_PAUSE))
            binding.playPauseButton.setImageResource(R.drawable.ic_play)
        }
        else {
            sendBroadcast((Intent(MusicService.ACTION_REPLAY)))
            binding.playPauseButton.setImageResource(R.drawable.ic_pause)
        }
        isPlaying = !isPlaying
    }

    private fun viewMusicList() {
        musicListRecyclerView = binding.musicListRecyclerView
        musicListRecyclerView.layoutManager = LinearLayoutManager(this)

        val musicList = loadMusic()
        musicListAdapter = MusicListAdapter(musicList) { musicItem ->
            playMusic(musicItem)
        }
        musicListRecyclerView.adapter = musicListAdapter
    }

    private fun loadMusic(): List<MusicItem> {
        val musicList = mutableListOf<MusicItem>()
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION
        )

        val cursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            MediaStore.Audio.Media.TITLE
        )

        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getLong(it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                val title = it.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                val artist = it.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                val duration = it.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))

                musicList.add(MusicItem(id, title, artist, duration))
            }
        }

        if (musicList.isEmpty()) {
            Log.e("MainActivity", "로드할 음악 파일 없음")
        } else {
            Log.d("MainActivity", "${musicList.size}개의 파일 로드 완료")
        }
        return musicList
    }

    private fun playMusic(musicItem: MusicItem) {
        val intent = Intent(this, MusicService::class.java).apply {
            putExtra("MusicUri", Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, musicItem.id.toString()))
            putExtra("MusicName", musicItem.title)
        }
        startService(intent)
        binding.musicTitleTextButton.text = musicItem.title

        binding.playPauseButton.setImageResource(R.drawable.ic_pause)
        isPlaying = true
    }

}