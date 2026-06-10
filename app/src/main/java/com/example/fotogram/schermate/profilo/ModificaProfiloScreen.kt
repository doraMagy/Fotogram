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

    val nomeUtente = viewModel.nomeUtente
    val bio = viewModel.bio
    val dataNascita = viewModel.dataNascita
    val datiValidi = viewModel.datiValidi
    val dataNascitaValida = viewModel.dataNascitaValida
    val caricamento = viewModel.caricamento
    val messaggioErrore = viewModel.messaggioErrore
    val salvataggioCompletato = viewModel.salvataggioCompletato

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