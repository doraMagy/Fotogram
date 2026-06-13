package com.example.fotogram.rete

import kotlinx.serialization.Serializable

@Serializable
data class AggiornaImmagineUtenteRequest(
    val base64: String
)