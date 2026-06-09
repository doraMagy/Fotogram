package com.example.fotogram.schermate.avvio

import androidx.lifecycle.ViewModel

class AvvioViewModel : ViewModel() {

    fun controllaPrimoAvvio(): Boolean {
        // Per ora simuliamo: non abbiamo ancora DataStore.
        // Più avanti qui controlleremo se esiste una sessione salvata.
        return true
    }

    fun controllaSessionePresente(): Boolean {
        // Per ora simuliamo: fingiamo che la sessione esista già.
        // Più avanti qui leggeremo il numero di sessione da DataStore.
        return true
    }
}