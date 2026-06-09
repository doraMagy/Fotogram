package com.example.fotogram.schermate.avvio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.fotogram.sessione.SessioneManager

@Composable
fun AvvioScreen(
    onVaiRegistrazione: () -> Unit,
    onVaiBacheca: () -> Unit
) {
    val context = LocalContext.current

    val viewModel: AvvioViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                AvvioViewModel(
                    sessioneManager = SessioneManager(context)
                )
            }
        }
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Fotogram",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Button(
            onClick = {
                viewModel.controllaSessione(
                    onSessionePresente = onVaiBacheca,
                    onSessioneAssente = onVaiRegistrazione
                )
            }
        ) {
            Text("Entra")
        }
    }
}