package com.example.fotogram.schermate.profilo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ModificaProfiloViewModel : ViewModel() {

    var nomeUtente by mutableStateOf("utente_demo")
        private set

    var bio by mutableStateOf("Amo fotografare posti random 📸")
        private set

    var dataNascita by mutableStateOf("12/04/2002")
        private set

    val datiValidi: Boolean
        get() = nomeUtente.isNotBlank() && bio.length <= 100

    fun aggiornaNomeUtente(nuovoNome: String) {
        if (nuovoNome.length <= 15) {
            nomeUtente = nuovoNome
        }
    }

    fun aggiornaBio(nuovaBio: String) {
        if (nuovaBio.length <= 100) {
            bio = nuovaBio
        }
    }

    fun aggiornaDataNascita(nuovaData: String) {
        dataNascita = nuovaData
    }

    fun salvaProfilo() {
        if (!datiValidi) {
            return
        }

        // Per ora non salviamo davvero.
        // Più avanti qui chiameremo Repository/API/DataStore.
    }
}