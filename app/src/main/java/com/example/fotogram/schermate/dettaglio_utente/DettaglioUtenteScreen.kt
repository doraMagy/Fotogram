package com.example.fotogram.schermate.dettaglio_utente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun DettaglioUtenteScreen(
    nomeUtente: String,
    onTornaIndietro: () -> Unit,
    onApriImmaginePost: (String) -> Unit,
    onApriMappaPost: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Dettaglio utente")
        Text("Utente: $nomeUtente")

        Button(
            onClick = {
                onApriImmaginePost("post_utente_1")
            }
        ) {
            Text("Apri immagine post")
        }

        Button(
            onClick = {
                onApriMappaPost("post_utente_1")
            }
        ) {
            Text("Apri mappa post")
        }

        Button(
            onClick = onTornaIndietro
        ) {
            Text("Indietro")
        }
    }
}