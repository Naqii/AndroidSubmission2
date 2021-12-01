package com.example.submissionsatu.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Favourites::class], version = 1)
abstract class FavouritesRoomDatabase : RoomDatabase() {

    abstract fun favouritesDao(): FavouritesDao

    companion object {
        @Volatile
        private var INSTANCE: FavouritesRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): FavouritesRoomDatabase {
            if (INSTANCE == null) {
                synchronized((FavouritesRoomDatabase::class.java)) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                    FavouritesRoomDatabase::class.java, "favourites_database")
                        .build()
                }
            }
            return INSTANCE as FavouritesRoomDatabase
        }
    }
}