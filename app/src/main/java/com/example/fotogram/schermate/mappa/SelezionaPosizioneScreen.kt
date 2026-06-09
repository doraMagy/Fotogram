package com.example.fotogram.schermate.mappa

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SelezionaPosizioneScreen(
    onConfermaPosizione: () -> Unit,
    viewModel: SelezionaPosizioneViewModel = viewModel()
) {
    val posizioneSelezionata = viewModel.posizioneSelezionata
    val nomeLuogo = viewModel.nomeLuogo
    val latitudine = viewModel.latitudine
    val longitudine = viewModel.longitudine

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Tocca il riquadro per scegliere una posizione finta.",
            style = MaterialTheme.typography.bodyMedium
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .clickable {
                    viewModel.selezionaPosizioneFinta()
                },
            contentAlignment = Alignment.Center
        ) {
            if (posizioneSelezionata) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Posizione selezionata",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = nomeLuogo,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                Text("Tocca qui per selezionare una posizione")
            }
        }

        if (posizioneSelezionata) {
            Text(
                text = "Latitudine: $latitudine",
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = "Longitudine: $longitudine",
                style = MaterialTheme.typography.bodySmall
            )
        }

        Button(
            onClick = {
                if (viewModel.confermaPosizione()) {
                    onConfermaPosizione()
                }
            },
            enabled = posizioneSelezionata,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Conferma posizione")
        }
    }
}