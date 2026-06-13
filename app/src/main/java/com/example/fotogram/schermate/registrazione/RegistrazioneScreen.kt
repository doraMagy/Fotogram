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
import com.example.fotogram.repository.UtenteRepository
import com.example.fotogram.rete.RemoteDataSource
import com.example.fotogram.sessione.SessioneManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import com.example.fotogram.util.base64ToImageBitmap
import com.example.fotogram.util.uriToBase64ConLimite

@Composable
fun RegistrazioneScreen(
    onRegistrazioneCompletata: () -> Unit
) {
    val context = LocalContext.current

    val viewModel: RegistrazioneViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                RegistrazioneViewModel(
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
    val immagineProfiloSelezionata = viewModel.immagineProfiloSelezionata
    val registrazionePossibile = viewModel.registrazionePossibile
    val registrazioneCompletata = viewModel.registrazioneCompletata
    val messaggioErrore = viewModel.messaggioErrore
    val caricamento = viewModel.caricamento
    val immagineProfiloBase64 = viewModel.immagineProfiloBase64
    val erroreNomeUtente = viewModel.erroreNomeUtente
    val erroreImmagineProfilo = viewModel.erroreImmagineProfilo

    val anteprimaImmagineProfilo = remember(immagineProfiloBase64) {
        base64ToImageBitmap(immagineProfiloBase64)
    }

    var registrazioneTentata by remember { mutableStateOf(false) }

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

        if (registrazioneTentata && erroreNomeUtente != null) {
            Text(
                text = erroreNomeUtente,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth()
            )
        }

        BoxImmagineProfiloRegistrazione(
            immagineProfiloSelezionata = immagineProfiloSelezionata,
            anteprimaImmagineProfilo = anteprimaImmagineProfilo,
            onClick = {
                launcherImmagineProfilo.launch("image/*")
            }
        )

        if (registrazioneTentata && erroreImmagineProfilo != null) {
            Text(
                text = erroreImmagineProfilo,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (
            messaggioErrore != null &&
            messaggioErrore != erroreNomeUtente &&
            messaggioErrore != erroreImmagineProfilo
        ) {
            Text(
                text = messaggioErrore,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Button(
            onClick = {
                registrazioneTentata = true
                viewModel.completaRegistrazione()
            },
            enabled = !caricamento,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (caricamento) {
                Text("Registrazione...")
            } else {
                Text("Completa registrazione")
            }
        }
    }
}

@Composable
fun BoxImmagineProfiloRegistrazione(
    immagineProfiloSelezionata: Boolean,
    anteprimaImmagineProfilo: ImageBitmap?,
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
        if (anteprimaImmagineProfilo != null) {
            Image(
                bitmap = anteprimaImmagineProfilo,
                contentDescription = "Anteprima immagine profilo",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
        } else if (immagineProfiloSelezionata) {
            Text("Immagine profilo selezionata")
        } else {
            Text("Tocca per selezionare l'immagine profilo")
        }
    }
}