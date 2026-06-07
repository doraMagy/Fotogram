package com.example.fotogram.schermate.mappa

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SelezionaPosizioneScreen(
    onConfermaPosizione: () -> Unit,
    onAnnulla: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Seleziona posizione")
        Text("Qui in futuro ci sarà la mappa")

        Button(
            onClick = onConfermaPosizione
        ) {
            Text("Conferma posizione")
        }

        Button(
            onClick = onAnnulla
        ) {
            Text("Annulla")
        }
    }
}