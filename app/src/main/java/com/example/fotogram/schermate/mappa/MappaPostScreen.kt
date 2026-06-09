package com.example.fotogram.schermate.mappa

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
fun MappaPostScreen(
    idPost: String,
    viewModel: MappaPostViewModel = viewModel()
) {
    val posizioneCaricata = viewModel.posizioneCaricata
    val idPostCorrente = viewModel.idPost
    val nomeLuogo = viewModel.nomeLuogo
    val latitudine = viewModel.latitudine
    val longitudine = viewModel.longitudine

    LaunchedEffect(idPost) {
        viewModel.caricaPosizionePost(idPost)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (posizioneCaricata) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Mappa placeholder",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = nomeLuogo,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Text(
                text = "ID post: $idPostCorrente",
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = "Latitudine: $latitudine",
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = "Longitudine: $longitudine",
                style = MaterialTheme.typography.bodySmall
            )
        } else {
            Text("Caricamento posizione...")
        }
    }
}