package com.example.fotogram.schermate.bacheca

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.fotogram.model.Post
import com.example.fotogram.schermate.bacheca.SchedaPost

@Composable
fun BachecaScreen(
    onApriDettaglioUtente: (String) -> Unit,
    onApriImmaginePost: (String) -> Unit,
    onApriMappaPost: (String) -> Unit
) {
    val postDemo = listOf(
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

    LazyColumn(
        contentPadding = PaddingValues(
            vertical = 8.dp
        ),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(postDemo) { post ->
            SchedaPost(
                post = post,
                onApriDettaglioUtente = onApriDettaglioUtente,
                onApriImmaginePost = onApriImmaginePost,
                onApriMappaPost = onApriMappaPost
            )
        }
    }
}