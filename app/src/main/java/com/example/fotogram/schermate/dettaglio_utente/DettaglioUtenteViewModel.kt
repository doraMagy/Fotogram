package com.example.fotogram.schermate.dettaglio_utente

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.fotogram.model.Post
import com.example.fotogram.model.Utente

class DettaglioUtenteViewModel : ViewModel() {

    var seguito by mutableStateOf(false)
        private set

    var utente by mutableStateOf(
        Utente(
            nomeUtente = "",
            bio = "",
            dataNascita = "",
            numeroFollower = 0,
            numeroFollowing = 0,
            numeroPost = 0
        )
    )
        private set

    var postUtente by mutableStateOf<List<Post>>(emptyList())
        private set

    fun caricaUtente(nomeUtente: String) {
        utente = Utente(
            nomeUtente = nomeUtente,
            bio = "Bio di $nomeUtente",
            dataNascita = "20/05/2001",
            numeroFollower = 42,
            numeroFollowing = 18,
            numeroPost = 2
        )

        postUtente = listOf(
            Post(
                idPost = "post_${nomeUtente}_1",
                nomeAutore = nomeUtente,
                testo = "Post pubblicato da $nomeUtente",
                seguito = seguito,
                haPosizione = true,
                dataCreazione = "2026-06-07"
            ),
            Post(
                idPost = "post_${nomeUtente}_2",
                nomeAutore = nomeUtente,
                testo = "Secondo post di esempio.",
                seguito = seguito,
                haPosizione = false,
                dataCreazione = "2026-06-06"
            )
        )
    }

    fun cambiaFollow() {
        seguito = !seguito

        postUtente = postUtente.map { post ->
            post.copy(
                seguito = seguito
            )
        }
    }
}