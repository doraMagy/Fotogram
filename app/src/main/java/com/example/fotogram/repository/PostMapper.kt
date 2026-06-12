package com.example.fotogram.repository

import com.example.fotogram.database.PostEntity
import com.example.fotogram.model.Post
import com.example.fotogram.rete.PostResponse

fun PostResponse.toPost(
    nomeAutore: String,
    seguito: Boolean
): Post {
    return Post(
        idPost = id.toString(),
        idAutore = authorId,
        nomeAutore = nomeAutore.ifBlank { "Utente $authorId" },
        testo = contentText?.takeIf { it.isNotBlank() } ?: "Nessuna descrizione",
        seguito = seguito,
        haPosizione = location != null,
        dataCreazione = createdAt ?: ""
    )
}

//serve per quando si scarica un post dal server
fun PostResponse.toPostEntity(
    nomeAutore: String,
    seguito: Boolean
): PostEntity {
    return PostEntity(
        idPost = id,
        idAutore = authorId,
        nomeAutore = nomeAutore.ifBlank { "Utente $authorId" },
        testo = contentText?.takeIf { it.isNotBlank() } ?: "Nessuna descrizione",
        seguito = seguito,
        dataCreazione = createdAt ?: "",
        immagineBase64 = contentPicture,
        latitudine = location?.latitude,
        longitudine = location?.longitude
    )
}

//serve quando il post è già salvato in locale. Viene letto da Room e trasformato
fun PostEntity.toPost(): Post {
    return Post(
        idPost = idPost.toString(),
        idAutore = idAutore,
        nomeAutore = nomeAutore,
        testo = testo,
        seguito = seguito,
        haPosizione = latitudine != null && longitudine != null,
        dataCreazione = dataCreazione
    )
}

//PostResponse dato del server, Entity salvato in Room, Post mostrato dalla UI.