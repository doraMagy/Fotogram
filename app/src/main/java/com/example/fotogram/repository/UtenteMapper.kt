package com.example.fotogram.repository

import com.example.fotogram.model.Utente
import com.example.fotogram.rete.UserResponse

//aggiungendo funzione alla classe UserResponse della rete
fun UserResponse.toUtente(): Utente {
    return Utente(
        nomeUtente = username?.takeIf { it.isNotBlank() } ?: "Utente $id",
        bio = bio?.takeIf { it.isNotBlank() } ?: "Bio mancante",
        dataNascita = dateOfBirth ?: "",
        numeroFollower = followersCount,
        numeroFollowing = followingCount,
        numeroPost = postsCount,
        immagineProfiloBase64 = profilePicture
    )
}