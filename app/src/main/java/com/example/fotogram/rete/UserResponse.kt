package com.example.fotogram.rete

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: Int,
    val createdAt: String? = null,
    val username: String? = null,
    val bio: String? = null,
    val dateOfBirth: String? = null,
    val profilePicture: String? = null,
    val isYourFollower: Boolean = false,
    val isYourFollowing: Boolean = false,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val postsCount: Int = 0
)