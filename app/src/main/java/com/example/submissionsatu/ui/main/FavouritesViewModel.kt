package com.example.submissionsatu.ui.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.submissionsatu.database.Favourites
import com.example.submissionsatu.repository.FavouritesRepository

class FavouritesViewModel(application: Application) : ViewModel() {
    private val mFavouritesRepository: FavouritesRepository = FavouritesRepository(application)
    fun getAllFav(): LiveData<List<Favourites>> = mFavouritesRepository.getAllFavourites()
}