package com.example.fotogram.repository

import com.example.fotogram.model.Utente
import com.example.fotogram.rete.AggiornaUtenteRequest
import com.example.fotogram.rete.RemoteDataSource
import com.example.fotogram.sessione.SessioneManager

class UtenteRepository(
    private val remoteDataSource: RemoteDataSource,
    private val sessioneManager: SessioneManager
) {

    suspend fun registraUtente(nomeUtente: String) {
        val loginResponse = remoteDataSource.loginUtente()

        sessioneManager.salvaSessione(
            numeroSessione = loginResponse.sessionId,
            userId = loginResponse.userId
        )

        remoteDataSource.aggiornaUtente(
            sessionId = loginResponse.sessionId,
            request = AggiornaUtenteRequest(
                username = nomeUtente,
                bio = "",
                dateOfBirth = null
            )
        )
    }

    suspend fun caricaProfiloPersonale(): Utente {
        val sessionId = sessioneManager.leggiNumeroSessione()
            ?: throw Exception("Sessione non trovata")

        val userId = sessioneManager.leggiUserId()
            ?: throw Exception("Utente non trovato")

        val userResponse = remoteDataSource.caricaUtente(
            sessionId = sessionId,
            userId = userId
        )

        return userResponse.toUtente()
    }

    suspend fun logout() {
        sessioneManager.eliminaSessione()
    }

    suspend fun aggiornaProfilo(
        nomeUtente: String,
        bio: String,
        dataNascita: String?
    ): Utente {
        val sessionId = sessioneManager.leggiNumeroSessione()
            ?: throw Exception("Sessione non trovata")

        val userResponse = remoteDataSource.aggiornaUtente(
            sessionId = sessionId,
            request = AggiornaUtenteRequest(
                username = nomeUtente,
                bio = bio,
                dateOfBirth = dataNascita
            )
        )

        return userResponse.toUtente()
    }

    suspend fun caricaDettaglioUtente(idUtente: Int): Pair<Utente, Boolean> {
        val sessionId = sessioneManager.leggiNumeroSessione()
            ?: throw Exception("Sessione non trovata")

        val userResponse = remoteDataSource.caricaUtente(
            sessionId = sessionId,
            userId = idUtente
        )

        return Pair(
            userResponse.toUtente(),
            userResponse.isYourFollowing
        )
    }

    suspend fun seguiUtente(targetId: Int) {
        val sessionId = sessioneManager.leggiNumeroSessione()
            ?: throw Exception("Sessione non trovata")

        remoteDataSource.seguiUtente(
            sessionId = sessionId,
            targetId = targetId
        )
    }

    suspend fun smettiDiSeguireUtente(targetId: Int) {
        val sessionId = sessioneManager.leggiNumeroSessione()
            ?: throw Exception("Sessione non trovata")

        remoteDataSource.smettiDiSeguireUtente(
            sessionId = sessionId,
            targetId = targetId
        )
    }

    suspend fun leggiUserId(): Int {
        return sessioneManager.leggiUserId()
            ?: throw Exception("Utente non trovato")
    }

}