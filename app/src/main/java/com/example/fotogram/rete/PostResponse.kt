package com.example.fotogram.rete

import kotlinx.serialization.Serializable

@Serializable
data class PostResponse(
    val id: Int,
    val authorId: Int,
    val createdAt: String,
    val contentPicture: String,
    val contentText: String,
    val location: LocationResponse? = null
)