package com.example.fotogram.repository

import com.example.fotogram.model.Utente
import com.example.fotogram.rete.UserResponse

fun UserResponse.toUtente(): Utente {
    return Utente(
        nomeUtente = username ?: "Utente $id",
        bio = bio ?: "",
        dataNascita = dateOfBirth ?: "",
        numeroFollower = followersCount,
        numeroFollowing = followingCount,
        numeroPost = postsCount
    )
}