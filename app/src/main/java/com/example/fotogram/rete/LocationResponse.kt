package com.example.fotogram.rete

import kotlinx.serialization.Serializable

@Serializable
data class LocationResponse(
    val latitude: Double?,
    val longitude: Double?
)