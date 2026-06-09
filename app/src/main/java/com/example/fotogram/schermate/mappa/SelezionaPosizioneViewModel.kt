package com.example.fotogram.schermate.mappa

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SelezionaPosizioneViewModel : ViewModel() {

    var posizioneSelezionata by mutableStateOf(false)
        private set

    var nomeLuogo by mutableStateOf("")
        private set

    var latitudine by mutableStateOf(0.0)
        private set

    var longitudine by mutableStateOf(0.0)
        private set

    fun selezionaPosizioneFinta() {
        posizioneSelezionata = true
        nomeLuogo = "Milano Duomo"
        latitudine = 45.4642
        longitudine = 9.1900
    }

    fun confermaPosizione(): Boolean {
        return posizioneSelezionata
    }
}