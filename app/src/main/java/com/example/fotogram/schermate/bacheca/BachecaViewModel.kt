package com.example.fotogram.schermate.bacheca

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

    var postBacheca by mutableStateOf<List<Post>>(emptyList())
        private set

    var caricamento by mutableStateOf(false)
        private set

    var messaggioErrore by mutableStateOf<String?>(null)
        private set

    fun caricaBacheca() {
        if (postBacheca.isNotEmpty()) {
            return
        }

        viewModelScope.launch {
            caricamento = true
            messaggioErrore = null

            try {
                postBacheca = postRepository.caricaBacheca()
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
            caricamento = true
            messaggioErrore = null

            try {
                postBacheca = postRepository.caricaBacheca()
            } catch (errore: Exception) {
                messaggioErrore = errore.message ?: "Errore durante l'aggiornamento della bacheca"
            } finally {
                caricamento = false
            }
        }
    }

    //aggiornare Assist chip di scoperta/segui
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
    }

}