package com.example.fotogram.schermate.immagine

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotogram.model.Post
import com.example.fotogram.repository.PostRepository
import kotlinx.coroutines.launch

class ImmaginePostViewModel(
    private val postRepository: PostRepository
) : ViewModel() {

    var post by mutableStateOf<Post?>(null)
        private set

    var caricamento by mutableStateOf(false)
        private set

    var messaggioErrore by mutableStateOf<String?>(null)
        private set

    fun caricaPost(idPost: String) {
        if (caricamento || post != null) {
            return
        }

        val idPostIntero = idPost.toIntOrNull()

        if (idPostIntero == null) {
            messaggioErrore = "Post non valido"
            return
        }

        viewModelScope.launch {
            caricamento = true
            messaggioErrore = null

            try {
                post = postRepository.caricaPostDaCache(idPostIntero)

                if (post == null) {
                    messaggioErrore = "Immagine non disponibile"
                }
            } catch (errore: Exception) {
                messaggioErrore = errore.message ?: "Errore durante il caricamento dell'immagine"
            } finally {
                caricamento = false
            }
        }
    }
}