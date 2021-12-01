package com.example.submissionsatu.helper

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.submissionsatu.ui.insert.FavouritesAddUpdateViewModel
import com.example.submissionsatu.ui.main.FavouritesViewModel

class ViewModelFactory private constructor(private val mApplication: Application) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(application)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavouritesViewModel::class.java)) {
            return FavouritesViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(FavouritesAddUpdateViewModel::class.java)) {
            return FavouritesAddUpdateViewModel(mApplication) as T
        }
        throw IllegalArgumentException("Unknwon ViewModel class: ${modelClass.name}")
    }
}