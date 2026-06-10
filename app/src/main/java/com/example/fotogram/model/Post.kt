package com.example.fotogram.model

data class Post(
    val idPost: String,
    val idAutore: Int,
    val nomeAutore: String,
    val testo: String,
    val seguito: Boolean,
    val haPosizione: Boolean,
    val dataCreazione: String
)