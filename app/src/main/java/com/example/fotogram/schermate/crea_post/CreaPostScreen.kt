package com.example.fotogram.schermate.crea_post

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun CreaPostScreen(
    onTornaBacheca: () -> Unit,
    onSelezionaPosizione: () -> Unit
) {
    var testoPost by remember {
        mutableStateOf("")
    }

    var immagineSelezionata by remember {
        mutableStateOf(false)
    }

    var posizioneSelezionata by remember {
        mutableStateOf(false)
    }

    val testoValido = testoPost.isNotBlank()
    val postPubblicabile = testoValido && immagineSelezionata

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Immagine del post",
            style = MaterialTheme.typography.titleSmall
        )

        BoxImmaginePost(
            immagineSelezionata = immagineSelezionata,
            onClick = {
                immagineSelezionata = true
            }
        )

        Text(
            text = "Descrizione",
            style = MaterialTheme.typography.titleSmall
        )

        OutlinedTextField(
            value = testoPost,
            onValueChange = { nuovoTesto ->
                if (nuovoTesto.length <= 100) {
                    testoPost = nuovoTesto
                }
            },
            label = {
                Text("Testo del post")
            },
            placeholder = {
                Text("Scrivi un messaggio...")
            },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            maxLines = 3
        )

        Text(
            text = "${testoPost.length}/100 caratteri",
            style = MaterialTheme.typography.bodySmall
        )

        Text(
            text = "Posizione",
            style = MaterialTheme.typography.titleSmall
        )

        OutlinedButton(
            onClick = {
                posizioneSelezionata = true
                onSelezionaPosizione()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            if (posizioneSelezionata) {
                Text("Modifica posizione")
            } else {
                Text("Aggiungi posizione")
            }
        }

        if (posizioneSelezionata) {
            Text(
                text = "Posizione selezionata",
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Button(
            onClick = onTornaBacheca,
            enabled = postPubblicabile,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Pubblica")
        }
    }
}

@Composable
fun BoxImmaginePost(
    immagineSelezionata: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        if (immagineSelezionata) {
            Text("Anteprima immagine selezionata")
        } else {
            Text("Tocca per selezionare un'immagine")
        }
    }
}