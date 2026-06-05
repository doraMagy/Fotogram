package com.example.fotogram.schermate.avvio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun AvvioScreen(
    onVaiRegistrazione: () -> Unit,
    onVaiBacheca: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Fotogram")

        Button(
            onClick = onVaiRegistrazione
        ) {
            Text("Simula primo avvio")
        }

        Button(
            onClick = onVaiBacheca
        ) {
            Text("Simula sessione già presente")
        }
    }
}