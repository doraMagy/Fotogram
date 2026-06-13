package com.example.fotogram.schermate.crea_post

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotogram.repository.PostRepository
import kotlinx.coroutines.launch

class CreaPostViewModel(
    private val postRepository: PostRepository
) : ViewModel() {

    var testoPost by mutableStateOf("")
        private set

    var immagineSelezionata by mutableStateOf(false)
        private set

    var immagineBase64 by mutableStateOf<String?>(null)
        private set

    var posizioneSelezionata by mutableStateOf(false)
        private set

    var latitudine by mutableStateOf<Double?>(null)
        private set

    var longitudine by mutableStateOf<Double?>(null)
        private set

    var caricamento by mutableStateOf(false)
        private set

    var messaggioErrore by mutableStateOf<String?>(null)
        private set

    var postPubblicato by mutableStateOf(false)
        private set

    val erroreImmagine: String?
        get() = if (immagineBase64 == null) {
            "Seleziona un'immagine per pubblicare il post"
        } else {
            null
        }

    val erroreTesto: String?
        get() = when {
            testoPost.isBlank() -> "Scrivi una descrizione per pubblicare il post"
            testoPost.length > 100 -> "La descrizione può avere al massimo 100 caratteri"
            else -> null
        }

    val messaggioValidazione: String?
        get() = erroreImmagine ?: erroreTesto

    val pubblicazionePossibile: Boolean
        get() = messaggioValidazione == null && !caricamento

    fun aggiornaTestoPost(nuovoTesto: String) {
        if (nuovoTesto.length <= 100) {
            testoPost = nuovoTesto
            messaggioErrore = null
        }
    }

    fun aggiornaImmagine(base64: String) {
        immagineBase64 = base64
        immagineSelezionata = true
        messaggioErrore = null
    }

    fun segnalaErroreImmagine(messaggio: String) {
        immagineBase64 = null
        immagineSelezionata = false
        messaggioErrore = messaggio
    }

    fun aggiornaPosizione(
        lat: Double,
        lon: Double
    ) {
        posizioneSelezionata = true
        latitudine = lat
        longitudine = lon
    }

    fun rimuoviPosizione() {
        posizioneSelezionata = false
        latitudine = null
        longitudine = null
    }

    fun pubblicaPost() {
        val erroreValidazione = messaggioValidazione

        if (erroreValidazione != null) {
            return
        }

        val immagineDaPubblicare = immagineBase64 ?: return

        viewModelScope.launch {
            caricamento = true
            messaggioErrore = null

            try {
                postRepository.creaPost(
                    testo = testoPost,
                    immagineBase64 = immagineDaPubblicare,
                    latitudine = latitudine,
                    longitudine = longitudine
                )

                postPubblicato = true
            } catch (errore: Exception) {
                messaggioErrore = errore.message ?: "Errore durante la pubblicazione del post"
            } finally {
                caricamento = false
            }
        }
    }
}