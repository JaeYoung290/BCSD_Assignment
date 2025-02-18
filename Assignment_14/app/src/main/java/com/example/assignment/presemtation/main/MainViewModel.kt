package com.example.assignment.presemtation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment.data.api.GitHubApiClient
import com.example.assignment.data.model.GitHubRepo
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _repos = MutableLiveData<List<GitHubRepo>>()
    val repos: LiveData<List<GitHubRepo>> get() = _repos

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var currentPage = 1
    private var query = ""

    fun searchRepositories(newQuery: String) {
        if (_isLoading.value == true) return

        if (query != newQuery) {
            query = newQuery
            currentPage = 1
            _repos.value = emptyList()
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = GitHubApiClient.apiService.searchRepositories(query, page = currentPage)
                val currentList = _repos.value ?: emptyList()

                _repos.value = currentList + response.items
                currentPage++
            } catch (e: Exception) {
                e.printStackTrace()
            }
            _isLoading.value = false
        }
    }
}