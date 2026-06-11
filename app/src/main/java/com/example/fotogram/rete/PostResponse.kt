package com.example.fotogram.rete

import kotlinx.serialization.Serializable

@Serializable
data class PostResponse(
    val id: Int,
    val authorId: Int,
    val createdAt: String? = null,
    val contentPicture: String? = null,
    val contentText: String? = null,
    val location: LocationResponse? = null
)