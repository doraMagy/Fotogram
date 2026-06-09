package com.example.fotogram.schermate.registrazione

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class RegistrazioneViewModel : ViewModel() {

    var nomeUtente by mutableStateOf("")
        private set

    var immagineProfiloSelezionata by mutableStateOf(false)
        private set

    val nomeValido: Boolean
        get() = nomeUtente.isNotBlank() && nomeUtente.length <= 15

    val registrazionePossibile: Boolean
        get() = nomeValido && immagineProfiloSelezionata

    fun aggiornaNomeUtente(nuovoNome: String) {
        if (nuovoNome.length <= 15) {
            nomeUtente = nuovoNome
        }
    }

    fun selezionaImmagineProfilo() {
        immagineProfiloSelezionata = true
    }

    fun completaRegistrazione() {
        if (!registrazionePossibile) {
            return
        }

        // Per ora non chiamiamo ancora il server.
        // Più avanti qui otterremo il numero di sessione
        // e lo salveremo con DataStore.
    }
}