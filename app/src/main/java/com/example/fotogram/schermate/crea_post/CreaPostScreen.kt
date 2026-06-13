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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.fotogram.database.AppDatabase
import com.example.fotogram.repository.PostRepository
import com.example.fotogram.rete.RemoteDataSource
import com.example.fotogram.sessione.SessioneManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.remember
import androidx.compose.ui.layout.ContentScale
import com.example.fotogram.util.base64ToImageBitmap
import com.example.fotogram.util.uriToBase64ConLimite
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun CreaPostScreen(
    onPostPubblicato: () -> Unit,
    onSelezionaPosizione: () -> Unit,
    latitudineSelezionata: Double?,
    longitudineSelezionata: Double?,
    onPosizioneSelezionataGestita: () -> Unit
) {
    val context = LocalContext.current

    val viewModel: CreaPostViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                CreaPostViewModel(
                    postRepository = PostRepository(
                        remoteDataSource = RemoteDataSource(),
                        sessioneManager = SessioneManager(context),
                        postDao = AppDatabase.getDatabase(context).postDao()
                    )
                )
            }
        }
    )

    val launcherImmagine = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            try {
                val base64 = uriToBase64ConLimite(
                    context = context,
                    uri = uri
                )

                viewModel.aggiornaImmagine(base64)
            } catch (errore: Exception) {
                viewModel.segnalaErroreImmagine(
                    errore.message ?: "Errore durante la selezione dell'immagine"
                )
            }
        }
    }

    val testoPost = viewModel.testoPost
    val immagineSelezionata = viewModel.immagineSelezionata
    val posizioneSelezionata = viewModel.posizioneSelezionata
    val pubblicazionePossibile = viewModel.pubblicazionePossibile
    val postPubblicato = viewModel.postPubblicato
    val messaggioErrore = viewModel.messaggioErrore
    val caricamento = viewModel.caricamento
    val immagineBase64 = viewModel.immagineBase64
    val erroreImmagine = viewModel.erroreImmagine
    val erroreTesto = viewModel.erroreTesto
    val latitudine = viewModel.latitudine
    val longitudine = viewModel.longitudine

    val anteprimaImmagine = remember(immagineBase64) {
        base64ToImageBitmap(immagineBase64)
    }

    LaunchedEffect(postPubblicato) {
        if (postPubblicato) {
            onPostPubblicato()
        }
    }

    LaunchedEffect(latitudineSelezionata, longitudineSelezionata) {
        if (latitudineSelezionata != null && longitudineSelezionata != null) {
            viewModel.aggiornaPosizione(
                lat = latitudineSelezionata,
                lon = longitudineSelezionata
            )

            onPosizioneSelezionataGestita()
        }
    }

    var pubblicazioneTentata by remember { mutableStateOf(false) }

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
            anteprimaImmagine = anteprimaImmagine,
            onClick = {
                launcherImmagine.launch("image/*")
            }
        )

        if (pubblicazioneTentata && erroreImmagine != null) {
            Text(
                text = erroreImmagine,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

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

        if (pubblicazioneTentata && erroreTesto != null) {
            Text(
                text = erroreTesto,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Text(
            text = "Posizione (facoltativo)",
            style = MaterialTheme.typography.titleSmall
        )

        if (!posizioneSelezionata) {
            OutlinedButton(
                onClick = {
                    onSelezionaPosizione()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Aggiungi posizione")
            }
        } else {
            Text(
                text = "Latitudine: ${latitudine ?: "-"}, Longitudine: ${longitudine ?: "-"}",
                style = MaterialTheme.typography.bodyMedium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        onSelezionaPosizione()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Modifica")
                }

                OutlinedButton(
                    onClick = {
                        viewModel.rimuoviPosizione()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Rimuovi")
                }
            }
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        if (messaggioErrore != null) {
            Text(
                text = messaggioErrore,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Button(
            onClick = {
                pubblicazioneTentata = true
                viewModel.pubblicaPost()
            },
            enabled = !caricamento,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (caricamento) {
                Text("Pubblicazione...")
            } else {
                Text("Pubblica")
            }
        }
    }
}

@Composable
fun BoxImmaginePost(
    immagineSelezionata: Boolean,
    anteprimaImmagine: androidx.compose.ui.graphics.ImageBitmap?,
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
        if (anteprimaImmagine != null) {
            Image(
                bitmap = anteprimaImmagine,
                contentDescription = "Anteprima immagine selezionata",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
        } else if (immagineSelezionata) {
            Text("Immagine selezionata")
        } else {
            Text("Tocca per selezionare un'immagine")
        }
    }
}