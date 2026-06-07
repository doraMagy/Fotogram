package com.example.fotogram.schermate.profilo

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
fun ModificaProfiloScreen(
    onSalva: () -> Unit,
    onAnnulla: () -> Unit
) {
    var nomeUtente by remember {
        mutableStateOf("")
    }

    var bio by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Modifica profilo")

        OutlinedTextField(
            value = nomeUtente,
            onValueChange = {
                nomeUtente = it
            },
            label = {
                Text("Nome utente")
            }
        )

        OutlinedTextField(
            value = bio,
            onValueChange = {
                bio = it
            },
            label = {
                Text("Biografia")
            }
        )

        Button(
            onClick = onSalva
        ) {
            Text("Salva")
        }

        Button(
            onClick = onAnnulla
        ) {
            Text("Annulla")
        }
    }
}