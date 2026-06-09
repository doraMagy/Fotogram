package com.example.fotogram.sessione

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore(name = "sessione")

class SessioneManager(private val context: Context) {
    companion object {
        private val CHIAVE_SESSIONE = stringPreferencesKey("numero_sessione")
        private val CHIAVE_USER_ID = intPreferencesKey("user_id")
    }

    suspend fun salvaSessione(numeroSessione: String, userId: Int) {
        context.dataStore.edit { preferenze ->
            preferenze[CHIAVE_SESSIONE] = numeroSessione
            preferenze[CHIAVE_USER_ID] = userId
        }
    }

    suspend fun leggiNumeroSessione(): String? {
        val preferenze = context.dataStore.data.first()
        return preferenze[CHIAVE_SESSIONE]
    }

    suspend fun leggiUserId(): Int? {
        val preferenze = context.dataStore.data.first()
        return preferenze[CHIAVE_USER_ID]
    }

    suspend fun eliminaSessione() {
        context.dataStore.edit { preferenze ->
            preferenze.remove(CHIAVE_SESSIONE)
            preferenze.remove(CHIAVE_USER_ID)
        }
    }
}