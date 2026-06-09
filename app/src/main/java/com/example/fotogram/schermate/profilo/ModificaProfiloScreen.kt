package com.example.fotogram.schermate.profilo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ModificaProfiloScreen(
    onSalva: () -> Unit,
    viewModel: ModificaProfiloViewModel = viewModel()
) {
    val nomeUtente = viewModel.nomeUtente
    val bio = viewModel.bio
    val dataNascita = viewModel.dataNascita
    val datiValidi = viewModel.datiValidi

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
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Text(
            text = "Per ora i dati sono finti: il salvataggio reale arriverà con API/repository.",
            style = MaterialTheme.typography.bodySmall
        )

        Button(
            onClick = {
                viewModel.salvaProfilo()
                onSalva()
            },
            enabled = datiValidi,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salva")
        }
    }
}