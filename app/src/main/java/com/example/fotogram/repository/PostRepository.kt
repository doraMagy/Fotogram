package com.example.fotogram.repository

import com.example.fotogram.model.Post
import com.example.fotogram.rete.CreaPostRequest
import com.example.fotogram.rete.LocationResponse
import com.example.fotogram.rete.RemoteDataSource
import com.example.fotogram.sessione.SessioneManager

class PostRepository(
    private val remoteDataSource: RemoteDataSource,
    private val sessioneManager: SessioneManager
) {

    //ottenere bacheca
    suspend fun caricaBacheca(
        maxPostId: Int? = null,
        limit: Int = 10
    ): List<Post> {
        val sessionId = sessioneManager.leggiNumeroSessione()
            ?: throw Exception("Sessione non trovata")

        val idPost = remoteDataSource.caricaFeed(
            sessionId = sessionId,
            maxPostId = maxPostId,
            limit = limit
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

    //ottenere i post di un utente specifico
    suspend fun caricaPostDiUtente(idUtente: Int): List<Post> {
        val sessionId = sessioneManager.leggiNumeroSessione()
            ?: throw Exception("Sessione non trovata")

        val autore = remoteDataSource.caricaUtente(
            sessionId = sessionId,
            userId = idUtente
        )

        val idPost = remoteDataSource.caricaPostUtente(
            sessionId = sessionId,
            authorId = idUtente,
            limit = 10
        )

        return idPost.map { id ->
            val postResponse = remoteDataSource.caricaPost(
                sessionId = sessionId,
                postId = id
            )

            postResponse.toPost(
                nomeAutore = autore.username ?: "Utente $idUtente",
                seguito = autore.isYourFollowing
            )
        }
    }

    //creazione di un post
    suspend fun creaPost(
        testo: String,
        immagineBase64: String,
        latitudine: Double?,
        longitudine: Double?
    ): Post {
        val sessionId = sessioneManager.leggiNumeroSessione()
            ?: throw Exception("Sessione non trovata")

        val userId = sessioneManager.leggiUserId()
            ?: throw Exception("Utente non trovato")

        val posizione = if (latitudine != null && longitudine != null) {
            LocationResponse(
                latitude = latitudine,
                longitude = longitudine
            )
        } else {
            null
        }

        val postResponse = remoteDataSource.creaPost(
            sessionId = sessionId,
            request = CreaPostRequest(
                contentText = testo,
                contentPicture = immagineBase64,
                location = posizione
            )
        )

        val autore = remoteDataSource.caricaUtente(
            sessionId = sessionId,
            userId = userId
        )

        return postResponse.toPost(
            nomeAutore = autore.username ?: "Utente $userId",
            seguito = true
        )
    }

}