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

    var immagineProfiloBase64 by mutableStateOf<String?>(null)
        private set

    var registrazioneCompletata by mutableStateOf(false)
        private set

    var messaggioErrore by mutableStateOf<String?>(null)
        private set

    var caricamento by mutableStateOf(false)
        private set

    val erroreNomeUtente: String?
        get() = when {
            nomeUtente.isBlank() -> "Inserisci un nome utente"
            nomeUtente.length > 15 -> "Il nome utente può avere al massimo 15 caratteri"
            else -> null
        }

    val erroreImmagineProfilo: String?
        get() = if (immagineProfiloBase64 == null) {
            "Seleziona un'immagine profilo"
        } else {
            null
        }

    val messaggioValidazione: String?
        get() = erroreNomeUtente ?: erroreImmagineProfilo

    val registrazionePossibile: Boolean
        get() = messaggioValidazione == null && !caricamento

    fun aggiornaNomeUtente(nuovoNome: String) {
        if (nuovoNome.length <= 15) {
            nomeUtente = nuovoNome
            messaggioErrore = null
        }
    }

    fun aggiornaImmagineProfilo(base64: String) {
        immagineProfiloBase64 = base64
        immagineProfiloSelezionata = true
        messaggioErrore = null
    }

    fun segnalaErroreImmagine(messaggio: String) {
        immagineProfiloBase64 = null
        immagineProfiloSelezionata = false
        messaggioErrore = messaggio
    }

    fun completaRegistrazione() {
        val erroreValidazione = messaggioValidazione

        if (erroreValidazione != null) {
            return
        }

        val immagineDaSalvare = immagineProfiloBase64 ?: return

        viewModelScope.launch {
            caricamento = true
            messaggioErrore = null

            try {
                utenteRepository.registraUtente(
                    nomeUtente = nomeUtente,
                    immagineProfiloBase64 = immagineDaSalvare
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