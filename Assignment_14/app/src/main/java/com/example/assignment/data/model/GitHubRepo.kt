package com.example.assignment.data.model

data class GitHubRepo(
    val id: Long,
    val name: String,
    val html_url: String,
    val description: String?
)
