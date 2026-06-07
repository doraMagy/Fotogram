package com.example.fotogram.model

data class Utente(
    val nomeUtente: String,
    val bio: String,
    val dataNascita: String,
    val numeroFollower: Int,
    val numeroFollowing: Int,
    val numeroPost: Int
)