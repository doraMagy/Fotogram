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

            if (numeroSessione != null) {
                onSessionePresente()
            } else {
                onSessioneAssente()
            }
        }
    }
}