package com.example.assignment

/*
* 테스트 항목
* 1. 권한 허용
* 1-1. 권한 즉시 허용 시 > RecyclerView 즉시 생성 (0)
* 1-2. 권한 거절 이후 다이얼로그 창에서 확인 > 권한 요청 팝업 (0)
* 1-3. 권한 거절 이후 다이얼로그 창에서 취소 클릭 > 프래그먼트 표시 (0)
* 1-3-1. 프래그먼트에서 설정 창 이동 후 > 권한을 거절한 상태일 경우 앱에 복귀하면 프래그먼트 유지 (0)
* 1-3-2. 프래그먼트에서 설정 창 이동 후 > 권한을 허용하면 RecyclerView 생성 (0)
* 1-4. 권한 거절 이후 앱 종료 > 앱 재실행 시 다이얼로그 창 생성 (0)
* 1-4-1. 다이얼로그 창에서 확인 클릭 > 권한 요청 팝업 (x)
* 1-4-2. 다이얼로그 창에서 취소 클릭 > 프래그먼트 표시 (0)
* */

import android.Manifest
import android.annotation.SuppressLint
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment.databinding.ActivityMainBinding

const val permission = Manifest.permission.READ_MEDIA_AUDIO

class MainActivity : AppCompatActivity(), PermissionCallback {
    private lateinit var binding: ActivityMainBinding
    private lateinit var permissionHelper: PermissionHelper
    private lateinit var musicListAdapter: MusicListAdapter
    private lateinit var musicListRecyclerView: RecyclerView
    private var isFragmentVisible = false
    private var backPressedTime = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        permissionHelper = PermissionHelper(this, this)


        checkAndRequestPermissionInMainActivity()

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isFragmentVisible) {
                    if (System.currentTimeMillis() - backPressedTime <= 3000) {
                        finish()
                    } else {
                        backPressedTime = System.currentTimeMillis()
                        Toast.makeText(this@MainActivity, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
        this.onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onRestart() {
        super.onRestart()
        if (permissionHelper.hasPermission(permission)) {
            viewMusicList()
        }
    }

    override fun onResume() {
        super.onResume()
        if (permissionHelper.hasPermission(permission)) {
            viewMusicList()
        }
    }

    override fun onPermissionGranted(permission: String) {
        when (permission) {
            Manifest.permission.READ_MEDIA_AUDIO -> {
                viewMusicList()
            }
        }
    }

    private fun checkAndRequestPermissionInMainActivity() {
        permissionHelper.checkAndRequestPermissions(permission)

        if (!permissionHelper.hasPermission(permission)) {
            permissionHelper.showPermissionRequiredFragment()
            isFragmentVisible = true
        } else if (permissionHelper.hasPermission(permission)) {
            viewMusicList()
        }
    }

    private fun loadMusicFromRawFolder(): List<MusicItem> {
        val musicList = mutableListOf<MusicItem>()
        val rawFields = R.raw::class.java.fields

        for (field in rawFields) {
            val mediaMetadataRetriever = MediaMetadataRetriever()
            val assetFileDescriptor = resources.openRawResourceFd(field.getInt(null))

            mediaMetadataRetriever.setDataSource(
                assetFileDescriptor.fileDescriptor,
                assetFileDescriptor.startOffset,
                assetFileDescriptor.length
            )

            val title =
                mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                    ?: "Unknown Title"
            val artist =
                mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                    ?: "Unknown Artist"
            val durationMs =
                mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                    ?.toLong() ?: 0
            val duration = formatDuration(durationMs)

            musicList.add(MusicItem(title, artist, duration))

            mediaMetadataRetriever.release()
            assetFileDescriptor.close()
        }

        return musicList
    }

    @SuppressLint("DefaultLocale")
    private fun formatDuration(durationMs: Long): String {
        val seconds = (durationMs / 1000).toInt()
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
    }



    private fun viewMusicList() {
        musicListRecyclerView = findViewById(R.id.music_list_recycler_view)
        musicListRecyclerView.layoutManager = LinearLayoutManager(this)

        val musicList = loadMusicFromRawFolder()
        musicListAdapter = MusicListAdapter(musicList)
        musicListRecyclerView.adapter = musicListAdapter
        isFragmentVisible = false
    }
}