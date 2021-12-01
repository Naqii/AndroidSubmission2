package com.example.submissionsatu.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.submissionsatu.database.Favourites
import com.example.submissionsatu.database.FavouritesDao
import com.example.submissionsatu.database.FavouritesRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavouritesRepository(application: Application) {
    private val mFavouriteDao: FavouritesDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavouritesRoomDatabase.getDatabase(application)
        mFavouriteDao = db.favouritesDao()
    }

    fun getAllFavourites(): LiveData<List<Favourites>> = mFavouriteDao.getAllFav()

    fun insert(favourites: Favourites) {
        executorService.execute { mFavouriteDao.insert(favourites) }
    }

    fun delete(favourites: Favourites) {
        executorService.execute { mFavouriteDao.delete(favourites) }
    }
}