package com.example.fotogram.schermate.profilo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotogram.model.Post
import com.example.fotogram.model.Utente
import com.example.fotogram.sessione.SessioneManager
import kotlinx.coroutines.launch

class ProfiloViewModel(
    private val sessioneManager: SessioneManager
) : ViewModel() {

    var utente by mutableStateOf(
        Utente(
            nomeUtente = "utente_demo",
            bio = "Amo fotografare posti random 📸",
            dataNascita = "12/04/2002",
            numeroFollower = 128,
            numeroFollowing = 75,
            numeroPost = 3
        )
    )
        private set

    var postPersonali by mutableStateOf(
        listOf(
            Post(
                idPost = "mio_post_1",
                nomeAutore = "utente_demo",
                testo = "Il mio primo post su Fotogram!",
                seguito = true,
                haPosizione = true,
                dataCreazione = "2026-06-07"
            ),
            Post(
                idPost = "mio_post_2",
                nomeAutore = "utente_demo",
                testo = "Sto costruendo la mia app Android.",
                seguito = true,
                haPosizione = false,
                dataCreazione = "2026-06-06"
            ),
            Post(
                idPost = "mio_post_3",
                nomeAutore = "utente_demo",
                testo = "Test della schermata profilo.",
                seguito = true,
                haPosizione = true,
                dataCreazione = "2026-06-05"
            )
        )
    )
        private set

    //da togliere alla fine
    var logoutCompletato by mutableStateOf(false)
        private set

    //da togliere alla fine
    fun logout() {
        viewModelScope.launch {
            sessioneManager.eliminaSessione()
            logoutCompletato = true
        }
    }
}