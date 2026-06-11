package com.example.fotogram.rete

import kotlinx.serialization.Serializable

@Serializable
data class CreaPostRequest(
    val contentText: String,
    val contentPicture: String,
    val location: LocationResponse? = null
)