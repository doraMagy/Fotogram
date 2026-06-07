package com.example.fotogram.navigazione

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fotogram.schermate.avvio.AvvioScreen
import com.example.fotogram.schermate.bacheca.BachecaScreen
import com.example.fotogram.schermate.crea_post.CreaPostScreen
import com.example.fotogram.schermate.dettaglio_utente.DettaglioUtenteScreen
import com.example.fotogram.schermate.immagine.ImmaginePostScreen
import com.example.fotogram.schermate.mappa.MappaPostScreen
import com.example.fotogram.schermate.mappa.SelezionaPosizioneScreen
import com.example.fotogram.schermate.profilo.ModificaProfiloScreen
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
                onVaiProfilo = navigatore::vaiAlProfilo,
                onApriDettaglioUtente = navigatore::vaiADettaglioUtente,
                onApriImmaginePost = navigatore::vaiAImmaginePost,
                onApriMappaPost = navigatore::vaiAMappaPost
            )
        }

        composable(Schermata.CreaPost.route) {
            CreaPostScreen(
                onTornaBacheca = navigatore::vaiAllaBacheca,
                onSelezionaPosizione = navigatore::vaiASelezionaPosizione
            )
        }

        composable(Schermata.Profilo.route) {
            ProfiloScreen(
                onTornaBacheca = navigatore::vaiAllaBacheca,
                onModificaProfilo = navigatore::vaiAModificaProfilo,
                onApriImmaginePost = navigatore::vaiAImmaginePost,
                onApriMappaPost = navigatore::vaiAMappaPost
            )
        }

        composable(
            route = Schermata.DettaglioUtente.route,
            arguments = listOf(
                navArgument("nomeUtente") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val nomeUtente = backStackEntry.arguments?.getString("nomeUtente") ?: ""

            DettaglioUtenteScreen(
                nomeUtente = nomeUtente,
                onTornaIndietro = navigatore::tornaIndietro,
                onApriImmaginePost = navigatore::vaiAImmaginePost,
                onApriMappaPost = navigatore::vaiAMappaPost
            )
        }

        composable(
            route = Schermata.ImmaginePost.route,
            arguments = listOf(
                navArgument("idPost") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val idPost = backStackEntry.arguments?.getString("idPost") ?: ""

            ImmaginePostScreen(
                idPost = idPost,
                onTornaIndietro = navigatore::tornaIndietro
            )
        }

        composable(
            route = Schermata.MappaPost.route,
            arguments = listOf(
                navArgument("idPost") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val idPost = backStackEntry.arguments?.getString("idPost") ?: ""

            MappaPostScreen(
                idPost = idPost,
                onTornaIndietro = navigatore::tornaIndietro
            )
        }

        composable(Schermata.SelezionaPosizione.route) {
            SelezionaPosizioneScreen(
                onConfermaPosizione = navigatore::tornaIndietro,
                onAnnulla = navigatore::tornaIndietro
            )
        }

        composable(Schermata.ModificaProfilo.route) {
            ModificaProfiloScreen(
                onSalva = navigatore::tornaIndietro,
                onAnnulla = navigatore::tornaIndietro
            )
        }
    }
}