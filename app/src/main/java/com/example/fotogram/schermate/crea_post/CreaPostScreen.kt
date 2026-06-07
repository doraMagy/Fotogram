package com.example.fotogram.schermate.crea_post

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
fun CreaPostScreen(
    onTornaBacheca: () -> Unit,
    onSelezionaPosizione: () -> Unit
) {
    var testoPost by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = testoPost,
            onValueChange = {
                testoPost = it
            },
            label = {
                Text("Testo del post")
            }
        )

        Button(
            onClick = onSelezionaPosizione
        ) {
            Text("Aggiungi posizione")
        }

        Button(
            onClick = onTornaBacheca
        ) {
            Text("Pubblica")
        }
    }
}