package com.example.fotogram.schermate.profilo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.fotogram.repository.UtenteRepository
import com.example.fotogram.rete.RemoteDataSource
import com.example.fotogram.sessione.SessioneManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import com.example.fotogram.util.base64ToImageBitmap
import com.example.fotogram.util.uriToBase64ConLimite

@Composable
fun ModificaProfiloScreen(
    onSalva: () -> Unit
) {
    val context = LocalContext.current

    val viewModel: ModificaProfiloViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                ModificaProfiloViewModel(
                    utenteRepository = UtenteRepository(
                        remoteDataSource = RemoteDataSource(),
                        sessioneManager = SessioneManager(context)
                    )
                )
            }
        }
    )

    val launcherImmagineProfilo = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            try {
                val base64 = uriToBase64ConLimite(
                    context = context,
                    uri = uri
                )

                viewModel.aggiornaImmagineProfilo(base64)
            } catch (errore: Exception) {
                viewModel.segnalaErroreImmagine(
                    errore.message ?: "Errore durante la selezione dell'immagine"
                )
            }
        }
    }

    val nomeUtente = viewModel.nomeUtente
    val bio = viewModel.bio
    val dataNascita = viewModel.dataNascita
    val datiValidi = viewModel.datiValidi
    val dataNascitaValida = viewModel.dataNascitaValida
    val caricamento = viewModel.caricamento
    val messaggioErrore = viewModel.messaggioErrore
    val salvataggioCompletato = viewModel.salvataggioCompletato
    val imgProfilo = viewModel.imgProfilo
    val nuovaImgProfilo = viewModel.nuovaImgProfilo

    val immagineDaMostrare = nuovaImgProfilo ?: imgProfilo

    val anteprimaImmagineProfilo = remember(immagineDaMostrare) {
        base64ToImageBitmap(immagineDaMostrare)
    }

    LaunchedEffect(Unit) {
        viewModel.caricaProfilo()
    }

    LaunchedEffect(salvataggioCompletato) {
        if (salvataggioCompletato) {
            onSalva()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Immagine profilo",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.fillMaxWidth()
        )

        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .clickable {
                    launcherImmagineProfilo.launch("image/*")
                },
            contentAlignment = Alignment.Center
        ) {
            if (anteprimaImmagineProfilo != null) {
                Image(
                    bitmap = anteprimaImmagineProfilo,
                    contentDescription = "Immagine profilo",
                    modifier = Modifier.size(120.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text("IMG")
            }
        }

        Text(
            text = "Tocca l'immagine per cambiarla",
            style = MaterialTheme.typography.bodySmall
        )

        OutlinedTextField(
            value = nomeUtente,
            onValueChange = viewModel::aggiornaNomeUtente,
            label = {
                Text("Nome utente")
            },
            supportingText = {
                Text("${nomeUtente.length}/15 caratteri")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = bio,
            onValueChange = viewModel::aggiornaBio,
            label = {
                Text("Biografia")
            },
            supportingText = {
                Text("${bio.length}/100 caratteri")
            },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 4
        )

        OutlinedTextField(
            value = dataNascita,
            onValueChange = viewModel::aggiornaDataNascita,
            label = {
                Text("Data di nascita")
            },
            placeholder = {
                Text("yyyy-MM-dd")
            },
            supportingText = {
                if (!dataNascitaValida) {
                    Text("Inserisci una data valida nel formato yyyy-MM-dd")
                } else {
                    Text("Formato: yyyy-MM-dd")
                }
            },
            isError = !dataNascitaValida,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
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
                viewModel.salvaProfilo()
            },
            enabled = datiValidi,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (caricamento) {
                Text("Salvataggio...")
            } else {
                Text("Salva")
            }
        }
    }
}