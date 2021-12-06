package com.example.submissionsatu.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionsatu.databinding.ActivityFavouriteBinding
import com.example.submissionsatu.helper.ViewModelFactory
import com.example.submissionsatu.ui.main.FavouritesAdapter
import com.example.submissionsatu.ui.main.FavouritesViewModel

class FavouriteActivity : AppCompatActivity() {

    private var _activityFavouriteBinding: ActivityFavouriteBinding? = null
    private val binding get() = _activityFavouriteBinding

    private lateinit var adapter: FavouritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activityFavouriteBinding = ActivityFavouriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.title = "Favourites"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val favViewModel = obtainViewModel(this@FavouriteActivity)
        favViewModel.getAllFav().observe(this, { favList ->
            if (favList != null) {
                adapter.setListFavourites(favList)
                showLoading(false)
            }
        })

        adapter = FavouritesAdapter()

        binding?.favUser?.layoutManager = LinearLayoutManager(this)
        binding?.favUser?.setHasFixedSize(true)
        binding?.favUser?.adapter = adapter
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavouritesViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavouritesViewModel::class.java)
    }

    private fun showLoading(state: Boolean) {
        binding?.progressBar?.visibility = if (state) View.VISIBLE else View.INVISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityFavouriteBinding = null
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}