package com.example.assignment.feature_word.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.assignment.R
import com.example.assignment.databinding.ActivityMainBinding
import com.example.assignment.feature_word.presentation.words.WordsEvent
import com.example.assignment.feature_word.presentation.words.WordsViewModel
import com.example.assignment.feature_word.presentation.words.components.WordAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var wordAdapter: WordAdapter
    private val viewModel: WordsViewModel by viewModels()
    private val permission = Manifest.permission.READ_MEDIA_IMAGES

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.wordsViewModel = viewModel
        binding.lifecycleOwner = this

        wordAdapter = WordAdapter()

        if (!hasPermission()) {
            requestPermission()
        } else {
            updateRecyclerView()
            setupListeners()
        }
    }

    private fun updateRecyclerView() {
        binding.rvMainWordList.apply {
            adapter = wordAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun setupListeners() {
        wordAdapter.setOnDeleteClickListener { word ->
            viewModel.onEvent(WordsEvent.DeleteWord(word))
        }

        wordAdapter.setOnItemClickListener { word ->
            viewModel.setSelectedWord(word)
            word.imageUri?.let { uri ->
                Glide.with(this)
                    .load(uri)
                    .into(binding.ivMainImage)
            }
        }

        binding.btnMainDelete.setOnClickListener {
            viewModel.selectedWord.value?.let { word ->
                viewModel.onEvent(WordsEvent.DeleteWord(word))
            }
            binding.ivMainImage.setImageDrawable(null)
        }

        binding.btnMainEdit.setOnClickListener {
            viewModel.selectedWord.value?.let { word ->
                val intent = Intent(this, AddEditWordActivity::class.java).apply {
                    putExtra("wordId", word.id)
                    putExtra("imageUri", word.imageUri)
                }
                startActivity(intent)
            }
            viewModel.clearSelectedWord()
            binding.ivMainImage.setImageDrawable(null)
        }

        binding.btnMainAdd.setOnClickListener {
            val intent = Intent(this, AddEditWordActivity::class.java).apply {
                putExtra("wordId", -1)
            }
            startActivity(intent)
            viewModel.clearSelectedWord()
            binding.ivMainImage.setImageDrawable(null)
        }
    }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permission ->
            if (permission) {
                Toast.makeText(this, "모든 권한이 허용되었습니다", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }

    private fun hasPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        if (!hasPermission()) {
            permissionLauncher.launch(permission)
        }
    }
}