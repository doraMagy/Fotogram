package com.example.fotogram.schermate.registrazione

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun RegistrazioneScreen(onRegistrazioneCompletata: () -> Unit) {
    var nomeUtente by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("FOTOGRAM")

        OutlinedTextField(
            value = nomeUtente,
            onValueChange = {
                nomeUtente = it
            },
            label = {
                Text("Username")
            }
        )

        Text("Qual'è il tuo aspetto? img placeholder")

        Button(
            onClick = onRegistrazioneCompletata
        ) {
            Text("Completa registrazione")
        }
    }
}
