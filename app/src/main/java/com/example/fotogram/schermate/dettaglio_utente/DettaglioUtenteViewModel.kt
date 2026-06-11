package com.example.fotogram.schermate.dettaglio_utente

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

class DettaglioUtenteViewModel(
    private val utenteRepository: UtenteRepository,
    private val postRepository: PostRepository
) : ViewModel() {

    var seguito by mutableStateOf(false)
        private set

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

    var postUtente by mutableStateOf<List<Post>>(emptyList())
        private set

    var caricamento by mutableStateOf(false)
        private set

    var messaggioErrore by mutableStateOf<String?>(null)
        private set

    var followAggiornato by mutableStateOf<Pair<Int, Boolean>?>(null)
        private set

    fun caricaUtente(idUtente: Int) {
        viewModelScope.launch {
            caricamento = true
            messaggioErrore = null

            try {
                val risultatoUtente = utenteRepository.caricaDettaglioUtente(idUtente)

                utente = risultatoUtente.first
                seguito = risultatoUtente.second
                postUtente = postRepository.caricaPostDiUtente(idUtente)
            } catch (errore: Exception) {
                messaggioErrore = errore.message ?: "Errore durante il caricamento dell'utente"
            } finally {
                caricamento = false
            }
        }
    }

    //FOLLOW O UNFOLLOW
    fun cambiaFollow(idUtente: Int) {
        viewModelScope.launch {
            messaggioErrore = null

            try {
                val nuovoStatoFollow = !seguito

                if (nuovoStatoFollow) {
                    utenteRepository.seguiUtente(idUtente)

                    utente = utente.copy(
                        numeroFollower = utente.numeroFollower + 1
                    )
                } else {
                    utenteRepository.smettiDiSeguireUtente(idUtente)

                    utente = utente.copy(
                        numeroFollower = (utente.numeroFollower - 1).coerceAtLeast(0)
                    )
                }

                seguito = nuovoStatoFollow

                postUtente = postUtente.map { post ->
                    post.copy(
                        seguito = nuovoStatoFollow
                    )
                }

                followAggiornato = Pair(idUtente, nuovoStatoFollow)
            } catch (errore: Exception) {
                messaggioErrore = errore.message ?: "Errore durante l'aggiornamento del follow"
            }
        }
    }

    fun followAggiornatoGestito() {
        followAggiornato = null
    }


}