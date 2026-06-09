package com.example.fotogram.navigazione

import androidx.navigation.NavController

class Navigatore(
    private val navController: NavController
) {

    fun vaiAllaRegistrazione() {
        navController.navigate(Schermata.Registrazione.route)
    }

    fun vaiAllaBacheca() {
        navController.navigate(Schermata.Bacheca.route) {
            popUpTo(Schermata.Avvio.route) {
                inclusive = true
            }
        }
    }

    fun vaiACreaPost() {
        navController.navigate(Schermata.CreaPost.route)
    }

    fun vaiAlProfilo() {
        navController.navigate(Schermata.Profilo.route)
    }

    fun vaiADettaglioUtente(nomeUtente: String) {
        navController.navigate(
            Schermata.DettaglioUtente.creaRoute(nomeUtente)
        )
    }

    fun vaiAImmaginePost(idPost: String) {
        navController.navigate(
            Schermata.ImmaginePost.creaRoute(idPost)
        )
    }

    fun vaiAMappaPost(idPost: String) {
        navController.navigate(
            Schermata.MappaPost.creaRoute(idPost)
        )
    }

    fun vaiASelezionaPosizione() {
        navController.navigate(Schermata.SelezionaPosizione.route)
    }

    fun vaiAModificaProfilo() {
        navController.navigate(Schermata.ModificaProfilo.route)
    }

    fun tornaIndietro() {
        navController.popBackStack()
    }

    //provvisorio per testing - da togliere alla fine
    fun vaiAllaRegistrazioneDopoLogout() {
        navController.navigate(Schermata.Registrazione.route) {
            popUpTo(0) {
                inclusive = true
            }
        }
    }

}

