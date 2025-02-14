package com.example.assignment.feature_word.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.wordsViewModel = viewModel
        binding.lifecycleOwner = this

        wordAdapter = WordAdapter()

        updateRecyclerView()
        setupListeners()
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
        }

        binding.btnMainDelete.setOnClickListener {
            viewModel.selectedWord.value?.let { word ->
                viewModel.onEvent(WordsEvent.DeleteWord(word))
            }
        }

        binding.btnMainEdit.setOnClickListener {
            viewModel.selectedWord.value?.let { word ->
                val intent = Intent(this, AddEditWordActivity::class.java).apply {
                    putExtra("wordId", word.id)
                }
                startActivity(intent)
            }
            viewModel.clearSelectedWord()
        }

        binding.btnMainAdd.setOnClickListener {
            val intent = Intent(this, AddEditWordActivity::class.java).apply {
                putExtra("wordId", -1)
            }
            startActivity(intent)
            viewModel.clearSelectedWord()
        }
    }
}