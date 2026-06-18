package com.example.fotogram.model

data class Utente(
    val nomeUtente: String,
    val bio: String,
    val dataNascita: String,
    val numeroFollower: Int,
    val numeroFollowing: Int,
    val numeroPost: Int,
    val immagineProfiloBase64: String? = null,

    // Campi aggiunti da UserResponse
    val idUtente: Int = 0,
    val dataCreazione: String = "",
    val tiSegue: Boolean = false,
    val seguitoDaTe: Boolean = false
)