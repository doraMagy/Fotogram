package com.example.fotogram.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PostEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    //DAO
    abstract fun postDao(): PostDao

    companion object {
        //singleton
        @Volatile
        private var INSTANCE: AppDatabase? = null
        //singola istanza del database per tutta l'app
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                //costruzione database Room
                val nuovaIstanza = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fotogram_database"
                ).build()

                INSTANCE = nuovaIstanza
                nuovaIstanza
            }
        }
    }
}