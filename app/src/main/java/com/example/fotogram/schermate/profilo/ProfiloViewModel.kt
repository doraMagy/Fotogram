package com.example.fotogram.schermate.profilo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotogram.model.Post
import com.example.fotogram.model.Utente
import com.example.fotogram.repository.PostRepository
import com.example.fotogram.repository.UtenteRepository
import kotlinx.coroutines.launch

class ProfiloViewModel(
    private val utenteRepository: UtenteRepository,
    private val postRepository: PostRepository
) : ViewModel() {

    var utente by mutableStateOf(
        Utente(
            nomeUtente = "",
            bio = "",
            dataNascita = "",
            numeroFollower = 0,
            numeroFollowing = 0,
            numeroPost = 0
        )
    )
        private set

    var postPersonali by mutableStateOf<List<Post>>(emptyList())
        private set

    var caricamento by mutableStateOf(false)
        private set

    var messaggioErrore by mutableStateOf<String?>(null)
        private set

    //da togliere alla fine
    var logoutCompletato by mutableStateOf(false)
        private set

    fun caricaProfilo() {
        viewModelScope.launch {
            caricamento = true
            messaggioErrore = null

            try {
                val idUtente = utenteRepository.leggiUserId()

                utente = utenteRepository.caricaProfiloPersonale()

                postPersonali = postRepository.caricaPostDiUtente(
                    idUtente = idUtente
                )
            } catch (errore: Exception) {
                messaggioErrore = errore.message ?: "Errore durante il caricamento del profilo"
            } finally {
                caricamento = false
            }
        }
    }

    //da togliere alla fine
    fun logout() {
        viewModelScope.launch {
            utenteRepository.logout()
            logoutCompletato = true
        }
    }
}