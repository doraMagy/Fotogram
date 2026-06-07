package com.example.fotogram.componenti

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.fotogram.navigazione.Schermata

@Composable
fun BarraInferiore(
    schermataCorrente: String?,
    onVaiBacheca: () -> Unit,
    onVaiCreaPost: () -> Unit,
    onVaiProfilo: () -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = schermataCorrente == Schermata.Bacheca.route,
            onClick = onVaiBacheca,
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Bacheca"
                )
            },
            label = {
                Text("Bacheca")
            }
        )

        NavigationBarItem(
            selected = schermataCorrente == Schermata.CreaPost.route,
            onClick = onVaiCreaPost,
            icon = {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "Crea post"
                )
            },
            label = {
                Text("Crea")
            }
        )

        NavigationBarItem(
            selected = schermataCorrente == Schermata.Profilo.route,
            onClick = onVaiProfilo,
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profilo"
                )
            },
            label = {
                Text("Profilo")
            }
        )
    }
}