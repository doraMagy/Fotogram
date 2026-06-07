package com.example.fotogram.schermate.profilo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ProfiloScreen(
    onModificaProfilo: () -> Unit,
    onApriImmaginePost: (String) -> Unit,
    onApriMappaPost: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onModificaProfilo
        ) {
            Text("Modifica profilo")
        }

        Button(
            onClick = {
                onApriImmaginePost("mio_post_1")
            }
        ) {
            Text("Apri immagine mio post")
        }

        Button(
            onClick = {
                onApriMappaPost("mio_post_1")
            }
        ) {
            Text("Apri mappa mio post")
        }
    }
}