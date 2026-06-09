package com.example.fotogram.schermate.immagine

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ImmaginePostViewModel : ViewModel() {

    var idPost by mutableStateOf("")
        private set

    var descrizioneImmagine by mutableStateOf("")
        private set

    var immagineCaricata by mutableStateOf(false)
        private set

    fun caricaImmagine(idPostRicevuto: String) {
        idPost = idPostRicevuto
        descrizioneImmagine = "Immagine del post $idPostRicevuto"
        immagineCaricata = true
    }
}