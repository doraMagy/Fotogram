package com.example.fotogram.rete

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val sessionId: String,
    val userId: Int
)