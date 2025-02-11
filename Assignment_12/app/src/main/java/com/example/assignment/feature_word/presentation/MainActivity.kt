package com.example.assignment.feature_word.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test_1.databinding.ActivityMainBinding
import com.example.assignment.feature_word.domain.model.Word
import com.example.assignment.feature_word.presentation.words.WordsEvent
import com.example.assignment.feature_word.presentation.words.WordsViewModel
import com.example.assignment.feature_word.presentation.words.components.WordAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var wordAdapter: WordAdapter
    private val viewModel: WordsViewModel by viewModels()
    private var selectedWord: Word? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        wordAdapter = WordAdapter()

        updateRecyclerView()
        observeViewModel()
        setupListeners()
    }

    private fun updateRecyclerView() {
        binding.rvMainWordList.apply {
            adapter = wordAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun observeViewModel() {
        viewModel.state.observe(this) { state ->
            Log.d("MainActivity", "데이터 개수: ${state.words.size}")
            wordAdapter.setData(state.words)
        }
    }

    private fun setupListeners() {
        wordAdapter.setOnDeleteClickListener { word ->
            viewModel.onEvent(WordsEvent.DeleteWord(word))
        }

        wordAdapter.setOnItemClickListener { word ->
            selectedWord = word
            binding.tvMainWord.text = word.word
            binding.tvMainWordMeaning.text = word.meaning
        }

        binding.btnMainDelete.setOnClickListener {
            selectedWord?.let { word ->
                viewModel.onEvent(WordsEvent.DeleteWord(word))
            }
            binding.tvMainWord.text = ""
            binding.tvMainWordMeaning.text = ""
            selectedWord = null
        }

        binding.btnMainEdit.setOnClickListener {
            selectedWord?.let { word ->
                val intent = Intent(this, AddEditWordActivity::class.java).apply {
                    putExtra("wordId", word.id)
                }
                startActivity(intent)
            }
            binding.tvMainWord.text = ""
            binding.tvMainWordMeaning.text = ""
            selectedWord = null
        }

        binding.btnMainAdd.setOnClickListener {
            val intent = Intent(this, AddEditWordActivity::class.java).apply {
                putExtra("wordId", -1)
            }
            startActivity(intent)
        }
    }

}