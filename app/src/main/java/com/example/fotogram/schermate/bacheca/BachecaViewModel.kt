package com.example.fotogram.schermate.bacheca

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotogram.model.Post
import com.example.fotogram.repository.PostRepository
import kotlinx.coroutines.launch

class BachecaViewModel(
    private val postRepository: PostRepository
) : ViewModel() {

    companion object {
        private const val LIMITE_POST = 10
    }

    var postBacheca by mutableStateOf<List<Post>>(emptyList())
        private set

    var caricamento by mutableStateOf(false)
        private set

    var aggiornamento by mutableStateOf(false)
        private set

    var caricamentoAltriPost by mutableStateOf(false)
        private set

    var fineFeed by mutableStateOf(false)
        private set

    var messaggioErrore by mutableStateOf<String?>(null)
        private set

    private var ultimoPostId: Int? = null

    //prima apertura della bacheca, carica solo se la lista è vuota
    fun caricaBacheca() {
        if (postBacheca.isNotEmpty() || caricamento) {
            return
        }

        viewModelScope.launch {
            caricamento = true
            messaggioErrore = null

            try {
                val nuoviPost = postRepository.caricaBacheca(
                    maxPostId = null,
                    limit = LIMITE_POST
                )

                postBacheca = nuoviPost
                ultimoPostId = nuoviPost.lastOrNull()?.idPost?.toIntOrNull()
                fineFeed = nuoviPost.size < LIMITE_POST
            } catch (errore: Exception) {
                messaggioErrore = errore.message ?: "Errore durante il caricamento della bacheca"
            } finally {
                caricamento = false
            }
        }
    }

    //per fare pull to refresh
    fun aggiornaBacheca() {
        viewModelScope.launch {
            aggiornamento = true
            messaggioErrore = null
            fineFeed = false
            ultimoPostId = null

            try {
                val nuoviPost = postRepository.caricaBacheca(
                    maxPostId = null,
                    limit = LIMITE_POST
                )

                postBacheca = nuoviPost
                ultimoPostId = nuoviPost.lastOrNull()?.idPost?.toIntOrNull()
                fineFeed = nuoviPost.size < LIMITE_POST
            } catch (errore: Exception) {
                messaggioErrore = errore.message ?: "Errore durante l'aggiornamento della bacheca"
            } finally {
                aggiornamento = false
            }
        }
    }

    //caricamento post più vecchi
    fun caricaAltriPost() {
        val ultimoId = ultimoPostId ?: return

        if (caricamento || aggiornamento || caricamentoAltriPost || fineFeed) {
            return
        }

        viewModelScope.launch {
            caricamentoAltriPost = true
            messaggioErrore = null

            try {
                val nuoviPost = postRepository.caricaBacheca(
                    maxPostId = ultimoId - 1,
                    limit = LIMITE_POST
                )

                if (nuoviPost.isEmpty()) {
                    fineFeed = true
                } else {
                    postBacheca = (postBacheca + nuoviPost).distinctBy { it.idPost }
                    ultimoPostId = nuoviPost.lastOrNull()?.idPost?.toIntOrNull()

                    if (nuoviPost.size < LIMITE_POST) {
                        fineFeed = true
                    }
                }
            } catch (errore: Exception) {
                messaggioErrore = errore.message ?: "Errore durante il caricamento di altri post"
            } finally {
                caricamentoAltriPost = false
            }
        }
    }

    //aggiorna assist chip di scoperta/segui
    fun aggiornaFollowAutore(
        idAutore: Int,
        seguito: Boolean
    ) {
        postBacheca = postBacheca.map { post ->
            if (post.idAutore == idAutore) {
                post.copy(
                    seguito = seguito
                )
            } else {
                post
            }
        }

        viewModelScope.launch {
            try {
                postRepository.aggiornaSeguitoAutoreInCache(
                    idAutore = idAutore,
                    seguito = seguito
                )
            } catch (errore: Exception) {
                Log.d("BachecaViewModel", "Errore aggiornamento follow in cache: ${errore.message}")
            }
        }
    }
}