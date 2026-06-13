package com.example.fotogram.schermate.profilo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotogram.repository.UtenteRepository
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.GregorianCalendar

class ModificaProfiloViewModel(
    private val utenteRepository: UtenteRepository
) : ViewModel() {

    var nomeUtente by mutableStateOf("")
        private set

    var bio by mutableStateOf("")
        private set

    var dataNascita by mutableStateOf("")
        private set

    var caricamento by mutableStateOf(false)
        private set

    var salvataggioCompletato by mutableStateOf(false)
        private set

    var messaggioErrore by mutableStateOf<String?>(null)
        private set

    var imgProfilo by mutableStateOf<String?>(null)
        private set

    var nuovaImgProfilo by mutableStateOf<String?>(null)
        private set

    val datiValidi: Boolean
        get() = nomeUtente.isNotBlank() &&
                nomeUtente.length <= 15 &&
                bio.length <= 100 &&
                dataNascitaValida &&
                !caricamento

    fun caricaProfilo() {
        viewModelScope.launch {
            caricamento = true
            messaggioErrore = null

            try {
                val utente = utenteRepository.caricaProfiloPersonale()

                nomeUtente = utente.nomeUtente
                bio = utente.bio
                dataNascita = utente.dataNascita
                imgProfilo = utente.immagineProfiloBase64
            } catch (errore: Exception) {
                messaggioErrore = errore.message ?: "Errore durante il caricamento del profilo"
            } finally {
                caricamento = false
            }
        }
    }

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

    fun aggiornaImmagineProfilo(base64: String) {
        nuovaImgProfilo = base64
        messaggioErrore = null
    }

    fun segnalaErroreImmagine(messaggio: String) {
        messaggioErrore = messaggio
    }

    fun salvaProfilo() {
        if (!datiValidi) {
            return
        }

        viewModelScope.launch {
            caricamento = true
            messaggioErrore = null

            try {
                utenteRepository.aggiornaProfilo(
                    nomeUtente = nomeUtente,
                    bio = bio,
                    dataNascita = dataNascita.ifBlank { null }
                )

                nuovaImgProfilo?.let { immagine ->
                    utenteRepository.aggiornaImmagineProfilo(immagine)
                }

                salvataggioCompletato = true
            } catch (errore: Exception) {
                messaggioErrore = errore.message ?: "Errore durante il salvataggio del profilo"
            } finally {
                caricamento = false
            }
        }
    }

    private fun dataNascitaValida(data: String): Boolean {
        val regex = Regex("""\d{4}-\d{2}-\d{2}""")

        if (!regex.matches(data)) {
            return false
        }

        val parti = data.split("-")

        val anno = parti[0].toInt()
        val mese = parti[1].toInt()
        val giorno = parti[2].toInt()

        val calendario = GregorianCalendar()
        calendario.isLenient = false

        return try {
            calendario.set(anno, mese - 1, giorno)
            calendario.time

            val oggi = Calendar.getInstance()

            val dataMinima = GregorianCalendar()
            dataMinima.set(1900, Calendar.JANUARY, 1)

            !calendario.after(oggi) && !calendario.before(dataMinima)
        } catch (errore: Exception) {
            false
        }
    }

    val dataNascitaValida: Boolean
        get() {
            if (dataNascita.isBlank()) {
                return true
            }

            return dataNascitaValida(dataNascita)
        }
}