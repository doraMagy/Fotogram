package com.example.fotogram.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "post")
data class PostEntity(
    @PrimaryKey val idPost: Int,
    val idAutore: Int,
    val nomeAutore: String,
    val testo: String, //content text
    val seguito: Boolean,
    val dataCreazione: String,
    val immagineBase64: String?, //contentPicture
    val latitudine: Double?,
    val longitudine: Double?
)