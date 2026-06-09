package com.example.fotogram.schermate.registrazione

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotogram.sessione.SessioneManager
import kotlinx.coroutines.launch

class RegistrazioneViewModel(private val sessioneManager: SessioneManager) : ViewModel() {

    var nomeUtente by mutableStateOf("")
        private set

    var immagineProfiloSelezionata by mutableStateOf(false)
        private set

    var registrazioneCompletata by mutableStateOf(false)
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

        viewModelScope.launch {
            sessioneManager.salvaSessione(
                numeroSessione = "sessione_demo_123",
                userId = 1
            )

            registrazioneCompletata = true
        }
    }
}