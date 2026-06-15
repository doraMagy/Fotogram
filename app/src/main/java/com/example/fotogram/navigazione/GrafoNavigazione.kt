package com.example.fotogram.navigazione

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fotogram.navBar.BarraInferiore
import com.example.fotogram.navBar.BarraSuperiore
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

    val backStackEntry by navController.currentBackStackEntryAsState()
    val routeCorrente = backStackEntry?.destination?.route

    val mostraBarrePrincipali = routeCorrente in listOf(
        Schermata.Bacheca.route,
        Schermata.CreaPost.route,
        Schermata.Profilo.route
    )

    val mostraBarraSuperioreSecondaria = routeCorrente in listOf(
        Schermata.DettaglioUtente.route,
        Schermata.ImmaginePost.route,
        Schermata.MappaPost.route,
        Schermata.SelezionaPosizione.route,
        Schermata.ModificaProfilo.route
    )

    val titolo = when (routeCorrente) {
        Schermata.Bacheca.route -> "Bacheca"
        Schermata.CreaPost.route -> "Crea post"
        Schermata.Profilo.route -> "Profilo"
        Schermata.DettaglioUtente.route -> "Dettaglio utente"
        Schermata.ImmaginePost.route -> "Immagine"
        Schermata.MappaPost.route -> "Mappa"
        Schermata.SelezionaPosizione.route -> "Seleziona posizione"
        Schermata.ModificaProfilo.route -> "Modifica profilo"
        else -> "Fotogram"
    }

    Scaffold(
        topBar = {
            if (mostraBarrePrincipali) {
                BarraSuperiore(
                    titolo = titolo,
                    mostraIndietro = false,
                    onIndietro = {}
                )
            }

            if (mostraBarraSuperioreSecondaria) {
                BarraSuperiore(
                    titolo = titolo,
                    mostraIndietro = true,
                    onIndietro = navigatore::tornaIndietro
                )
            }
        },
        bottomBar = {
            if (mostraBarrePrincipali) {
                BarraInferiore(
                    schermataCorrente = routeCorrente,
                    onVaiBacheca = navigatore::vaiAllaBacheca,
                    onVaiCreaPost = navigatore::vaiACreaPost,
                    onVaiProfilo = navigatore::vaiAlProfilo
                )
            }
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = Schermata.Avvio.route,
            modifier = Modifier.padding(paddingValues)
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

            composable(Schermata.Bacheca.route) { bachecaBackStackEntry ->

                val idAutoreFollowAggiornato = bachecaBackStackEntry
                    .savedStateHandle
                    .get<Int>("idAutoreFollowAggiornato")

                val seguitoAggiornato = bachecaBackStackEntry
                    .savedStateHandle
                    .get<Boolean>("seguitoAggiornato")
                //controlla se qualcuno ha lasciato un val chiamato "seguitoAgg"

                BachecaScreen(
                    onApriDettaglioUtente = navigatore::vaiADettaglioUtente,
                    onApriImmaginePost = navigatore::vaiAImmaginePost,
                    onApriMappaPost = navigatore::vaiAMappaPost,
                    idAutoreFollowAggiornato = idAutoreFollowAggiornato,
                    seguitoAggiornato = seguitoAggiornato,
                    onFollowAggiornatoGestito = {
                        bachecaBackStackEntry.savedStateHandle.remove<Int>("idAutoreFollowAggiornato")
                        bachecaBackStackEntry.savedStateHandle.remove<Boolean>("seguitoAggiornato")
                    }
                    //quando la bacheca ha finito di usare queste info vengono cancellate.
                )
            }

            composable(Schermata.CreaPost.route) { creaPostBackStackEntry ->

                val latitudineSelezionata = creaPostBackStackEntry
                    .savedStateHandle
                    .get<Double>("latitudineSelezionata")

                val longitudineSelezionata = creaPostBackStackEntry
                    .savedStateHandle
                    .get<Double>("longitudineSelezionata")

                CreaPostScreen(
                    onPostPubblicato = navigatore::vaiAlProfilo,
                    onSelezionaPosizione = navigatore::vaiASelezionaPosizione,
                    latitudineSelezionata = latitudineSelezionata,
                    longitudineSelezionata = longitudineSelezionata,
                    onPosizioneSelezionataGestita = {
                        creaPostBackStackEntry.savedStateHandle.remove<Double>("latitudineSelezionata")
                        creaPostBackStackEntry.savedStateHandle.remove<Double>("longitudineSelezionata")
                    }
                )
            }

            composable(Schermata.Profilo.route) {
                ProfiloScreen(
                    onModificaProfilo = navigatore::vaiAModificaProfilo,
                    onApriImmaginePost = navigatore::vaiAImmaginePost,
                    onApriMappaPost = navigatore::vaiAMappaPost
                )
            }

            composable(
                route = Schermata.DettaglioUtente.route,
                arguments = listOf(
                    navArgument("idUtente") {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->

                val idUtente = backStackEntry.arguments?.getInt("idUtente") ?: 0

                DettaglioUtenteScreen(
                    idUtente = idUtente,
                    onApriImmaginePost = navigatore::vaiAImmaginePost,
                    onApriMappaPost = navigatore::vaiAMappaPost,
                    onFollowAggiornato = { idAutore, seguito ->
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("idAutoreFollowAggiornato", idAutore)

                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("seguitoAggiornato", seguito)
                    }
                    //prende la schermata precedente e salva nel suo savedStateHandle i valori.
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
                )
            }

            composable(Schermata.SelezionaPosizione.route) {
                SelezionaPosizioneScreen(
                    onConfermaPosizione = { latitudine, longitudine ->
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("latitudineSelezionata", latitudine)

                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("longitudineSelezionata", longitudine)

                        navigatore.tornaIndietro()
                    }
                )
            }

            composable(Schermata.ModificaProfilo.route) {
                ModificaProfiloScreen(
                    onSalva = navigatore::tornaIndietro
                )
            }
        }
    }
}