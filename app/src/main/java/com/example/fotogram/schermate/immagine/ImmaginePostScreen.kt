package com.example.fotogram.schermate.immagine

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ImmaginePostScreen(
    idPost: String,
    onTornaIndietro: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Immagine post a schermo intero")
        Text("ID post: $idPost")

        Button(
            onClick = onTornaIndietro
        ) {
            Text("Indietro")
        }
    }
}