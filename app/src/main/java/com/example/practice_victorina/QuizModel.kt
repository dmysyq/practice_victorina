package com.example.practice_victorina


data class Question(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctIndex: Int
)
