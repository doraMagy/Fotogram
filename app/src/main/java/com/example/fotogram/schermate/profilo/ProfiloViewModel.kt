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

    companion object {
        private const val LIMITE_POST = 10
    }

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

    var caricamentoAltriPost by mutableStateOf(false)
        private set

    var finePost by mutableStateOf(false)
        private set

    var messaggioErrore by mutableStateOf<String?>(null)
        private set

    private var idUtenteCorrente: Int? = null
    private var ultimoPostId: Int? = null

    fun caricaProfilo() {
        if (caricamento) {
            return
        }

        viewModelScope.launch {
            caricamento = true
            messaggioErrore = null
            finePost = false
            ultimoPostId = null

            try {
                val idUtente = utenteRepository.leggiUserId()
                idUtenteCorrente = idUtente

                utente = utenteRepository.caricaProfiloPersonale()

                val nuoviPost = postRepository.caricaPostDiUtente(
                    idUtente = idUtente,
                    maxPostId = null,
                    limit = LIMITE_POST
                )

                postPersonali = nuoviPost
                ultimoPostId = nuoviPost.lastOrNull()?.idPost?.toIntOrNull()
                finePost = nuoviPost.size < LIMITE_POST

            } catch (errore: Exception) {
                messaggioErrore = errore.message ?: "Errore durante il caricamento del profilo"
            } finally {
                caricamento = false
            }
        }
    }

    fun caricaAltriPost() {
        val idUtente = idUtenteCorrente ?: return
        val ultimoId = ultimoPostId ?: return

        if (caricamento || caricamentoAltriPost || finePost) {
            return
        }

        viewModelScope.launch {
            caricamentoAltriPost = true
            messaggioErrore = null

            try {
                val nuoviPost = postRepository.caricaPostDiUtente(
                    idUtente = idUtente,
                    maxPostId = ultimoId - 1,
                    limit = LIMITE_POST
                )

                if (nuoviPost.isEmpty()) {
                    finePost = true
                } else {
                    postPersonali = (postPersonali + nuoviPost).distinctBy { it.idPost }
                    ultimoPostId = nuoviPost.lastOrNull()?.idPost?.toIntOrNull()

                    if (nuoviPost.size < LIMITE_POST) {
                        finePost = true
                    }
                }

            } catch (errore: Exception) {
                messaggioErrore = errore.message ?: "Errore durante il caricamento di altri post"
            } finally {
                caricamentoAltriPost = false
            }
        }
    }
}