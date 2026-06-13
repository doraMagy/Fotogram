package com.example.fotogram.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

//operazioni che vengono fatte sul database
@Dao
interface PostDao {
    //controlla se è già salvato localmente
    @Query("SELECT * FROM post WHERE idPost = :idPost")
    suspend fun cercaPost(idPost: Int): PostEntity?

    //salva il post nel database, se ci sono conflitti sovrascrive
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salvaPost(post: PostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salvaPost(post: List<PostEntity>)

    //aggiorna Room a follow/unfollow
    @Query("UPDATE post SET seguito = :seguito WHERE idAutore = :idAutore")
    suspend fun aggiornaSeguitoAutore(
        idAutore: Int,
        seguito: Boolean
    )

    @Query("UPDATE post SET nomeAutore = :nomeAutore, seguito = :seguito WHERE idAutore = :idAutore")
    suspend fun aggiornaDatiAutore(
        idAutore: Int,
        nomeAutore: String,
        seguito: Boolean
    )

    @Query("DELETE FROM post")
    suspend fun eliminaTuttiPost()
}