package com.example.fotogram.schermate.bacheca

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.fotogram.model.Post

class BachecaViewModel : ViewModel() {

    var postBacheca by mutableStateOf(
        listOf(
            Post(
                idPost = "post_1",
                nomeAutore = "utente_demo",
                testo = "Prima foto su Fotogram!",
                seguito = true,
                haPosizione = true,
                dataCreazione = "2026-06-07"
            ),
            Post(
                idPost = "post_2",
                nomeAutore = "luna22",
                testo = "Tramonto bellissimo oggi.",
                seguito = false,
                haPosizione = false,
                dataCreazione = "2026-06-07"
            ),
            Post(
                idPost = "post_3",
                nomeAutore = "marco_dev",
                testo = "Sto testando la bacheca dell'app.",
                seguito = true,
                haPosizione = true,
                dataCreazione = "2026-06-06"
            ),
            Post(
                idPost = "post_4",
                nomeAutore = "ale_photo",
                testo = "Foto random dalla galleria.",
                seguito = false,
                haPosizione = true,
                dataCreazione = "2026-06-06"
            )
        )
    )
        private set
}