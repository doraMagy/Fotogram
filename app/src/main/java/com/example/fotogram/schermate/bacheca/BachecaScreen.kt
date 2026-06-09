package com.example.fotogram.schermate.bacheca

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun BachecaScreen(
    onApriDettaglioUtente: (String) -> Unit,
    onApriImmaginePost: (String) -> Unit,
    onApriMappaPost: (String) -> Unit,
    viewModel: BachecaViewModel = viewModel()
) {
    val postBacheca = viewModel.postBacheca

    LazyColumn(
        contentPadding = PaddingValues(
            vertical = 8.dp
        ),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(postBacheca) { post ->
            SchedaPost(
                post = post,
                onApriDettaglioUtente = onApriDettaglioUtente,
                onApriImmaginePost = onApriImmaginePost,
                onApriMappaPost = onApriMappaPost
            )
        }
    }
}