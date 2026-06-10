package com.example.fotogram.schermate.registrazione

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotogram.repository.UtenteRepository
import kotlinx.coroutines.launch

class RegistrazioneViewModel(
    private val utenteRepository: UtenteRepository
) : ViewModel() {

    var nomeUtente by mutableStateOf("")
        private set

    var immagineProfiloSelezionata by mutableStateOf(false)
        private set

    var registrazioneCompletata by mutableStateOf(false)
        private set

    var messaggioErrore by mutableStateOf<String?>(null)
        private set

    var caricamento by mutableStateOf(false)
        private set

    val nomeValido: Boolean
        get() = nomeUtente.isNotBlank() && nomeUtente.length <= 15

    val registrazionePossibile: Boolean
        get() = nomeValido && immagineProfiloSelezionata && !caricamento

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
            caricamento = true
            messaggioErrore = null

            try {
                utenteRepository.registraUtente(
                    nomeUtente = nomeUtente
                )
                registrazioneCompletata = true
            } catch (errore: Exception) {
                messaggioErrore = errore.message ?: "Errore durante la registrazione"
            } finally {
                caricamento = false
            }
        }
    }
}