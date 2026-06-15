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
    //popUpTo() = pulisce una parte dello stack, "rimuovi tutto fino ad avvio compreso"

    fun vaiACreaPost() {
        navController.navigate(Schermata.CreaPost.route)
    }

    fun vaiAlProfilo() {
        navController.navigate(Schermata.Profilo.route)
    }

    fun vaiADettaglioUtente(idUtente: Int) {
        navController.navigate(
            Schermata.DettaglioUtente.creaRoute(idUtente)
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
        //rimuove la schermata corrente e torna alla precedente
    }
}

