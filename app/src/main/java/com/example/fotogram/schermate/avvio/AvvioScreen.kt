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
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AvvioScreen(
    onVaiRegistrazione: () -> Unit,
    onVaiBacheca: () -> Unit,
    viewModel: AvvioViewModel = viewModel()
) {
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
                val primoAvvio = viewModel.controllaPrimoAvvio()

                if (primoAvvio) {
                    onVaiRegistrazione()
                }
            }
        ) {
            Text("Simula primo avvio")
        }

        Button(
            onClick = {
                val sessionePresente = viewModel.controllaSessionePresente()

                if (sessionePresente) {
                    onVaiBacheca()
                }
            }
        ) {
            Text("Simula sessione già presente")
        }
    }
}