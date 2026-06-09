package com.example.fotogram.schermate.immagine

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ImmaginePostScreen(
    idPost: String,
    viewModel: ImmaginePostViewModel = viewModel()
) {
    val immagineCaricata = viewModel.immagineCaricata
    val descrizioneImmagine = viewModel.descrizioneImmagine
    val idPostCorrente = viewModel.idPost

    LaunchedEffect(idPost) {
        viewModel.caricaImmagine(idPost)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (immagineCaricata) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = descrizioneImmagine,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Text(
                text = "ID post: $idPostCorrente",
                style = MaterialTheme.typography.bodySmall
            )
        } else {
            Text("Caricamento immagine...")
        }
    }
}