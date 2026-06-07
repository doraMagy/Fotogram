package com.example.fotogram.schermate.bacheca

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun BachecaScreen(
    onVaiCreaPost: () -> Unit,
    onVaiProfilo: () -> Unit,
    onApriDettaglioUtente: (String) -> Unit,
    onApriImmaginePost: (String) -> Unit,
    onApriMappaPost: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bacheca")

        Button(
            onClick = onVaiCreaPost
        ) {
            Text("Crea post")
        }

        Button(
            onClick = onVaiProfilo
        ) {
            Text("Profilo")
        }

        Button(
            onClick = {
                onApriDettaglioUtente("utente_demo")
            }
        ) {
            Text("Apri dettaglio utente")
        }

        Button(
            onClick = {
                onApriImmaginePost("post_1")
            }
        ) {
            Text("Apri immagine post")
        }

        Button(
            onClick = {
                onApriMappaPost("post_1")
            }
        ) {
            Text("Apri mappa post")
        }
    }
}