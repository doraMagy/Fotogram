package com.example.fotogram

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.fotogram.navigazione.Schermata
import com.example.fotogram.schermate.avvio.AvvioScreen
import com.example.fotogram.schermate.bacheca.BachecaScreen
import com.example.fotogram.schermate.crea_post.CreaPostScreen
import com.example.fotogram.schermate.profilo.ProfiloScreen
import com.example.fotogram.schermate.registrazione.RegistrazioneScreen

@Composable
fun FotogramApp() {
    var schermataCorrente by remember { mutableStateOf(Schermata.AVVIO) }

    when (schermataCorrente) {
        Schermata.AVVIO -> {
            AvvioScreen(
                onVaiRegistrazione = {
                    schermataCorrente = Schermata.REGISTRAZIONE
                },
                onVaiBacheca = {
                    schermataCorrente = Schermata.BACHECA
                }
            )
        }

        Schermata.REGISTRAZIONE -> {
            RegistrazioneScreen(
                onRegistrazioneCompletata = {
                    schermataCorrente = Schermata.BACHECA
                }
            )
        }

        Schermata.BACHECA -> {
            BachecaScreen(
                onVaiCreaPost = {
                    schermataCorrente = Schermata.CREA_POST
                },
                onVaiProfilo = {
                    schermataCorrente = Schermata.PROFILO
                }
            )
        }

        Schermata.CREA_POST -> {
            CreaPostScreen(
                onTornaBacheca = {
                    schermataCorrente = Schermata.BACHECA
                }
            )
        }

        Schermata.PROFILO -> {
            ProfiloScreen(
                onTornaBacheca = {
                    schermataCorrente = Schermata.BACHECA
                }
            )
        }
    }
}