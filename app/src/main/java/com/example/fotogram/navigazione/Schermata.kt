package com.example.fotogram.navigazione

sealed class Schermata(val route: String) {

    data object Avvio : Schermata("avvio")

    data object Registrazione : Schermata("registrazione")

    data object Bacheca : Schermata("bacheca")

    data object CreaPost : Schermata("crea_post")

    data object Profilo : Schermata("profilo")

    data object DettaglioUtente : Schermata("dettaglio_utente/{idUtente}") {
        fun creaRoute(idUtente: Int): String {
            return "dettaglio_utente/$idUtente"
        }
    }

    data object ImmaginePost : Schermata("immagine_post/{idPost}") {
        fun creaRoute(idPost: String): String {
            return "immagine_post/$idPost"
        }
    }

    data object MappaPost : Schermata("mappa_post/{idPost}") {
        fun creaRoute(idPost: String): String {
            return "mappa_post/$idPost"
        }
    }

    data object SelezionaPosizione : Schermata("seleziona_posizione")

    data object ModificaProfilo : Schermata("modifica_profilo")
}