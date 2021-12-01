package com.example.submissionsatu.ui.insert

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.submissionsatu.database.Favourites
import com.example.submissionsatu.repository.FavouritesRepository

class FavouritesAddUpdateViewModel(application: Application) : ViewModel() {
    private val mFavouritesRepository: FavouritesRepository = FavouritesRepository(application)

    fun insert(fav: Favourites) {
        mFavouritesRepository.insert(fav)
    }

    fun delete(fav: Favourites) {
        mFavouritesRepository.delete(fav)
    }
}