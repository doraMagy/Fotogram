package com.example.fotogram.schermate.mappa

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MappaPostViewModel : ViewModel() {

    var idPost by mutableStateOf("")
        private set

    var nomeLuogo by mutableStateOf("")
        private set

    var latitudine by mutableStateOf(0.0)
        private set

    var longitudine by mutableStateOf(0.0)
        private set

    var posizioneCaricata by mutableStateOf(false)
        private set

    fun caricaPosizionePost(idPostRicevuto: String) {
        idPost = idPostRicevuto

        // Dati finti per ora.
        // Più avanti arriveranno dal server o dalla cache locale.
        nomeLuogo = when (idPostRicevuto) {
            "post_1" -> "Milano Duomo"
            "post_3" -> "Parco Sempione"
            "post_4" -> "Navigli"
            "mio_post_1" -> "Università degli Studi di Milano"
            "mio_post_3" -> "Stazione Centrale"
            else -> "Posizione del post"
        }

        latitudine = when (idPostRicevuto) {
            "post_1" -> 45.4642
            "post_3" -> 45.4722
            "post_4" -> 45.4520
            "mio_post_1" -> 45.4600
            "mio_post_3" -> 45.4861
            else -> 45.4642
        }

        longitudine = when (idPostRicevuto) {
            "post_1" -> 9.1900
            "post_3" -> 9.1736
            "post_4" -> 9.1750
            "mio_post_1" -> 9.1940
            "mio_post_3" -> 9.2057
            else -> 9.1900
        }

        posizioneCaricata = true
    }
}