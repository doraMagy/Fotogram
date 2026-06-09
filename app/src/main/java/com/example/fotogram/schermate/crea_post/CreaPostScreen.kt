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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CreaPostScreen(
    onPostPubblicato: () -> Unit,
    onSelezionaPosizione: () -> Unit,
    viewModel: CreaPostViewModel = viewModel()
) {
    val testoPost = viewModel.testoPost
    val immagineSelezionata = viewModel.immagineSelezionata
    val posizioneSelezionata = viewModel.posizioneSelezionata
    val postPubblicabile = viewModel.postPubblicabile

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
            onClick = viewModel::selezionaImmagine
        )

        Text(
            text = "Descrizione",
            style = MaterialTheme.typography.titleSmall
        )

        OutlinedTextField(
            value = testoPost,
            onValueChange = viewModel::aggiornaTestoPost,
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
                viewModel.selezionaPosizione()
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
            onClick = {
                viewModel.pubblicaPost()
                onPostPubblicato()
            },
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