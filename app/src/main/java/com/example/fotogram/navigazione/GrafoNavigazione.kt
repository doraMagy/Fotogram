package com.example.fotogram.navigazione

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fotogram.schermate.avvio.AvvioScreen
import com.example.fotogram.schermate.bacheca.BachecaScreen
import com.example.fotogram.schermate.crea_post.CreaPostScreen
import com.example.fotogram.schermate.profilo.ProfiloScreen
import com.example.fotogram.schermate.registrazione.RegistrazioneScreen

@Composable
fun GrafoNavigazione() {

    val navController = rememberNavController()

    val navigatore = remember(navController) {
        Navigatore(navController)
    }

    NavHost(
        navController = navController,
        startDestination = Schermata.Avvio.route
    ) {

        composable(Schermata.Avvio.route) {
            AvvioScreen(
                onVaiRegistrazione = navigatore::vaiAllaRegistrazione,
                onVaiBacheca = navigatore::vaiAllaBacheca
            )
        }

        composable(Schermata.Registrazione.route) {
            RegistrazioneScreen(
                onRegistrazioneCompletata = navigatore::vaiAllaBacheca
            )
        }

        composable(Schermata.Bacheca.route) {
            BachecaScreen(
                onVaiCreaPost = navigatore::vaiACreaPost,
                onVaiProfilo = navigatore::vaiAlProfilo
            )
        }

        composable(Schermata.CreaPost.route) {
            CreaPostScreen(
                onTornaBacheca = navigatore::vaiAllaBacheca
            )
        }

        composable(Schermata.Profilo.route) {
            ProfiloScreen(
                onTornaBacheca = navigatore::vaiAllaBacheca
            )
        }
    }
}