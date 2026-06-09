package com.example.fotogram.schermate.registrazione

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.fotogram.sessione.SessioneManager

@Composable
fun RegistrazioneScreen(
    onRegistrazioneCompletata: () -> Unit
) {
    val context = LocalContext.current

    val viewModel: RegistrazioneViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                RegistrazioneViewModel(
                    sessioneManager = SessioneManager(context)
                )
            }
        }
    )

    val nomeUtente = viewModel.nomeUtente
    val immagineProfiloSelezionata = viewModel.immagineProfiloSelezionata
    val registrazionePossibile = viewModel.registrazionePossibile
    val registrazioneCompletata = viewModel.registrazioneCompletata

    LaunchedEffect(registrazioneCompletata) {
        if (registrazioneCompletata) {
            onRegistrazioneCompletata()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(
            space = 20.dp,
            alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "FOTOGRAM",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = nomeUtente,
            onValueChange = viewModel::aggiornaNomeUtente,
            label = {
                Text("Username")
            },
            supportingText = {
                Text("${nomeUtente.length}/15 caratteri")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        BoxImmagineProfiloRegistrazione(
            immagineProfiloSelezionata = immagineProfiloSelezionata,
            onClick = viewModel::selezionaImmagineProfilo
        )

        Button(
            onClick = {
                viewModel.completaRegistrazione()
            },
            enabled = registrazionePossibile,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Completa registrazione")
        }
    }
}

@Composable
fun BoxImmagineProfiloRegistrazione(
    immagineProfiloSelezionata: Boolean,
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
        if (immagineProfiloSelezionata) {
            Text("Immagine profilo selezionata")
        } else {
            Text("Tocca per selezionare l'immagine profilo")
        }
    }
}