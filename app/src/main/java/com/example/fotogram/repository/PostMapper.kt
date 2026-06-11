package com.example.fotogram.repository

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