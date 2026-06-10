package com.example.fotogram.rete

import kotlinx.serialization.Serializable

@Serializable
data class AggiornaUtenteRequest(
    val username: String,
    val bio: String? = null,
    val dateOfBirth: String? = null
)