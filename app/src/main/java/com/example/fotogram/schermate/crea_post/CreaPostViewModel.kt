package com.example.fotogram.schermate.crea_post

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CreaPostViewModel : ViewModel() {

    var testoPost by mutableStateOf("")
        private set

    var immagineSelezionata by mutableStateOf(false)
        private set

    var posizioneSelezionata by mutableStateOf(false)
        private set

    val testoValido: Boolean
        get() = testoPost.isNotBlank()

    val postPubblicabile: Boolean
        get() = testoValido && immagineSelezionata

    fun aggiornaTestoPost(nuovoTesto: String) {
        if (nuovoTesto.length <= 100) {
            testoPost = nuovoTesto
        }
    }

    fun selezionaImmagine() {
        immagineSelezionata = true
    }

    fun selezionaPosizione() {
        posizioneSelezionata = true
    }

    fun pubblicaPost() {
        if (!postPubblicabile) {
            return
        }

        // Per ora non facciamo ancora API o salvataggi reali.
        // Più avanti qui chiameremo il Repository.
        resetCampi()
    }

    private fun resetCampi() {
        testoPost = ""
        immagineSelezionata = false
        posizioneSelezionata = false
    }
}