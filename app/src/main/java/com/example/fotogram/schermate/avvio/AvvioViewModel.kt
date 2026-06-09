package com.example.fotogram.schermate.avvio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fotogram.sessione.SessioneManager
import kotlinx.coroutines.launch

class AvvioViewModel(
    private val sessioneManager: SessioneManager
) : ViewModel() {

    fun controllaSessione(
        onSessionePresente: () -> Unit,
        onSessioneAssente: () -> Unit
    ) {
        viewModelScope.launch {
            val numeroSessione = sessioneManager.leggiNumeroSessione()
            val userId = sessioneManager.leggiUserId()

            if (numeroSessione != null && userId != null) {
                onSessionePresente()
            } else {
                onSessioneAssente()
            }
        }
    }
}