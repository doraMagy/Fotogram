package com.example.fotogram.repository

import com.example.fotogram.model.Post
import com.example.fotogram.rete.RemoteDataSource
import com.example.fotogram.sessione.SessioneManager

class PostRepository(
    private val remoteDataSource: RemoteDataSource,
    private val sessioneManager: SessioneManager
) {

    //ottenere bacheca
    suspend fun caricaBacheca(): List<Post> {
        val sessionId = sessioneManager.leggiNumeroSessione()
            ?: throw Exception("Sessione non trovata")

        val idPost = remoteDataSource.caricaFeed(
            sessionId = sessionId,
            limit = 10
        )

        return idPost.map { id ->
            val postResponse = remoteDataSource.caricaPost(
                sessionId = sessionId,
                postId = id
            )

            val autore = remoteDataSource.caricaUtente(
                sessionId = sessionId,
                userId = postResponse.authorId
            )

            postResponse.toPost(
                nomeAutore = autore.username ?: "Utente ${postResponse.authorId}",
                seguito = autore.isYourFollowing
            )
        }
    }
}