package com.example.assignment.feature_word.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.example.test_1.databinding.ActivityAddEditWordBinding
import com.example.assignment.feature_word.presentation.add_edit_word.AddEditWordEvent
import com.example.assignment.feature_word.presentation.add_edit_word.AddEditWordViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddEditWordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditWordBinding
    private val viewModel: AddEditWordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditWordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val wordId = intent.getIntExtra("wordId", -1)
        if (wordId != -1) {
            viewModel.onEvent(AddEditWordEvent.EnteredWordName(intent.getStringExtra("wordName") ?: ""))
            viewModel.onEvent(AddEditWordEvent.EnteredMeaning(intent.getStringExtra("wordMeaning") ?: ""))
        }

        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        viewModel.wordName.observe(this) { word ->
            if (binding.etAddEditWord.text.toString() != word) {
                binding.etAddEditWord.setText(word)
            }
        }

        viewModel.wordMeaning.observe(this) { meaning ->
            if (binding.etAddEditMeaning.text.toString() != meaning) {
                binding.etAddEditMeaning.setText(meaning)
            }
        }

        lifecycleScope.launch {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is AddEditWordViewModel.UiEvent.SaveWord -> {
                        finish()
                    }
                    is AddEditWordViewModel.UiEvent.ShowSnackbar -> {
                        Snackbar.make(
                            binding.root,
                            event.message,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }

                    else -> {}
                }
            }
        }
    }

    private fun setupListeners() {
        binding.etAddEditWord.addTextChangedListener { text ->
            viewModel.onEvent(AddEditWordEvent.EnteredWordName(text.toString()))
        }

        binding.etAddEditMeaning.addTextChangedListener { text ->
            viewModel.onEvent(AddEditWordEvent.EnteredMeaning(text.toString()))
        }

        binding.btnAddWord.setOnClickListener {
            val wordName = binding.etAddEditWord.text.toString()
            val wordMeaning = binding.etAddEditMeaning.text.toString()

            if(wordName.isBlank() || wordMeaning.isBlank()) {
                Snackbar.make(
                    binding.root,
                    "단어와 뜻을 모두 입력해야합니다.",
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            viewModel.onEvent(AddEditWordEvent.SaveWord)
        }
    }
}