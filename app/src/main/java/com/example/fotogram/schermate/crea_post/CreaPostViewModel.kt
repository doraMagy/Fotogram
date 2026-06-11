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

    val pubblicazionePossibile: Boolean
        get() = testoPost.isNotBlank() &&
                testoPost.length <= 100 &&
                immagineSelezionata &&
                !caricamento

    fun aggiornaTestoPost(nuovoTesto: String) {
        if (nuovoTesto.length <= 100) {
            testoPost = nuovoTesto
        }
    }

    fun selezionaImmagine() {
        immagineSelezionata = true
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
        if (!pubblicazionePossibile) {
            return
        }

        viewModelScope.launch {
            caricamento = true
            messaggioErrore = null

            try {
                postRepository.creaPost(
                    testo = testoPost,
                    immagineBase64 = IMMAGINE_BASE64_TEMPORANEA,
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

    companion object {
        private const val IMMAGINE_BASE64_TEMPORANEA =
            "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/x8AAwMCAO+/p9sAAAAASUVORK5CYII="
    }
}