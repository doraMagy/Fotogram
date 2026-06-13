package com.example.fotogram.repository

import com.example.fotogram.database.PostDao
import com.example.fotogram.model.Post
import com.example.fotogram.rete.CreaPostRequest
import com.example.fotogram.rete.LocationResponse
import com.example.fotogram.rete.RemoteDataSource
import com.example.fotogram.sessione.SessioneManager
import android.util.Log

class PostRepository(
    private val remoteDataSource: RemoteDataSource,
    private val sessioneManager: SessioneManager,
    private val postDao: PostDao
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
            caricaPostConCache(
                sessionId = sessionId,
                idPost = id
            )
        }
    }

    //ottenere i post di un utente specifico
    suspend fun caricaPostDiUtente(
        idUtente: Int,
        maxPostId: Int? = null,
        limit: Int = 10
    ): List<Post> {
        val sessionId = sessioneManager.leggiNumeroSessione()
            ?: throw Exception("Sessione non trovata")

        val autore = remoteDataSource.caricaUtente(
            sessionId = sessionId,
            userId = idUtente
        )

        val nomeAutoreAggiornato = autore.username ?: "Utente $idUtente"
        val seguitoAggiornato = autore.isYourFollowing

        postDao.aggiornaDatiAutore(
            idAutore = idUtente,
            nomeAutore = nomeAutoreAggiornato,
            seguito = seguitoAggiornato
        )

        val idPost = remoteDataSource.caricaPostUtente(
            sessionId = sessionId,
            authorId = idUtente,
            maxPostId = maxPostId,
            limit = limit
        )

        return idPost.map { id ->
            caricaPostConCache(
                sessionId = sessionId,
                idPost = id,
                nomeAutoreGiaCaricato = nomeAutoreAggiornato,
                seguitoGiaCaricato = seguitoAggiornato,
                immagineProfiloAutoreGiaCaricata = autore.profilePicture
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

        val postEntity = postResponse.toPostEntity(
            nomeAutore = autore.username ?: "Utente $userId",
            seguito = true
        )

        postDao.salvaPost(postEntity)

        Log.d("PostRepository", "Nuovo post ${postEntity.idPost} creato e salvato in Room")

        return postEntity.toPost()
    }

    //caricamento dei post da Room
    private suspend fun caricaPostConCache(
        sessionId: String,
        idPost: Int,
        nomeAutoreGiaCaricato: String? = null,
        seguitoGiaCaricato: Boolean? = null,
        immagineProfiloAutoreGiaCaricata: String? = null
    ): Post {
        val postInCache = postDao.cercaPost(idPost)

        if (postInCache != null) {
            Log.d("PostRepository", "Post $idPost trovato in cache Room")

            val nomeAutoreAggiornato: String
            val seguitoAggiornato: Boolean
            val immagineProfiloAutoreAggiornata: String?

            if (nomeAutoreGiaCaricato != null && seguitoGiaCaricato != null) {
                nomeAutoreAggiornato = nomeAutoreGiaCaricato
                seguitoAggiornato = seguitoGiaCaricato
                immagineProfiloAutoreAggiornata = immagineProfiloAutoreGiaCaricata
            } else {
                val autore = remoteDataSource.caricaUtente(
                    sessionId = sessionId,
                    userId = postInCache.idAutore
                )

                nomeAutoreAggiornato = autore.username ?: "Utente ${postInCache.idAutore}"
                seguitoAggiornato = autore.isYourFollowing
                immagineProfiloAutoreAggiornata = autore.profilePicture
            }

            val postAggiornato = postInCache.copy(
                nomeAutore = nomeAutoreAggiornato,
                seguito = seguitoAggiornato
            )

            postDao.salvaPost(postAggiornato)

            postDao.aggiornaDatiAutore(
                idAutore = postInCache.idAutore,
                nomeAutore = nomeAutoreAggiornato,
                seguito = seguitoAggiornato
            )

            Log.d("PostRepository", "Post $idPost aggiornato in cache con autore: $nomeAutoreAggiornato")

            return postAggiornato.toPost().copy(
                immagineProfiloAutoreBase64 = immagineProfiloAutoreAggiornata
            )
        }

        Log.d("PostRepository", "Post $idPost non in cache, scarico dal server")

        val postResponse = remoteDataSource.caricaPost(
            sessionId = sessionId,
            postId = idPost
        )

        val nomeAutore: String
        val seguito: Boolean
        val immagineProfiloAutore: String?

        if (nomeAutoreGiaCaricato != null && seguitoGiaCaricato != null) {
            nomeAutore = nomeAutoreGiaCaricato
            seguito = seguitoGiaCaricato
            immagineProfiloAutore = immagineProfiloAutoreGiaCaricata

            Log.d("PostRepository", "Autore del post $idPost già caricato: $nomeAutore")
        } else {
            val autore = remoteDataSource.caricaUtente(
                sessionId = sessionId,
                userId = postResponse.authorId
            )

            nomeAutore = autore.username ?: "Utente ${postResponse.authorId}"
            seguito = autore.isYourFollowing
            immagineProfiloAutore = autore.profilePicture

            Log.d("PostRepository", "Autore del post $idPost scaricato: $nomeAutore")
        }

        val postEntity = postResponse.toPostEntity(
            nomeAutore = nomeAutore,
            seguito = seguito
        )

        postDao.salvaPost(postEntity)

        Log.d("PostRepository", "Post $idPost salvato in Room")

        return postEntity.toPost().copy(
            immagineProfiloAutoreBase64 = immagineProfiloAutore
        )
    }

    suspend fun aggiornaSeguitoAutoreInCache(
        idAutore: Int,
        seguito: Boolean
    ) {
        postDao.aggiornaSeguitoAutore(
            idAutore = idAutore,
            seguito = seguito
        )

        Log.d("PostRepository", "Cache Room aggiornata: autore $idAutore seguito = $seguito")
    }

    suspend fun caricaPostDaCache(idPost: Int): Post? {
        val postEntity = postDao.cercaPost(idPost)

        return postEntity?.toPost()
    }

}