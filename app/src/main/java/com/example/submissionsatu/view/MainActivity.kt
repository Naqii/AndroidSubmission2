package com.example.submissionsatu.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionsatu.R
import com.example.submissionsatu.SettingPreferences
import com.example.submissionsatu.adapter.ListUserAdapter
import com.example.submissionsatu.databinding.ActivityMainBinding
import com.example.submissionsatu.model.User
import com.example.submissionsatu.viewmodel.*

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: ListUserAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var getUserModel: MainViewModel
    private lateinit var searchUserModel: SearchViewModel

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "User GitHub"

        binding.rvUser

        showDataUsers()
        showRecyclerList()
        showLoading(true)

        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            SettingsViewModel::class.java
        )
        settingViewModel.getThemeSettings().observe(this,
            { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    return@observe
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    return@observe
                }
            })
    }

    //List User
    private fun showDataUsers() {
        getUserModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)
        getUserModel.setMainUser()
        getUserModel.getMainUser().observe(this, { listUser ->
            if (listUser != null) {
                adapter.setData(listUser)
                showLoading(false)
            }
        })
    }

    private fun showRecyclerList() {
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvUser.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvUser.layoutManager = LinearLayoutManager(this)
        }
        adapter = ListUserAdapter()
        binding.rvUser.adapter = adapter
        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user: User) {
        val intent = Intent(this, DetailUser::class.java)
        intent.putExtra(DetailUser.EXTRA_DATA, user)
        startActivity(intent)

        Toast.makeText(this, "Your Choice " + user.username, Toast.LENGTH_SHORT).show()
    }

    //Search User
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isEmpty()) {
                    return true
                } else {
                    shearchDataUser(query)
                }
                showLoading(true)
//                shearchDataUser(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
//                if (newText.isEmpty()) {
//                    showDataUsers()
//                } else {
//                    shearchDataUser(newText)
//                }
//                showLoading(true)
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu1 -> {
                val s = Intent(this, SettingsActivity::class.java)
                startActivity(s)
                return true
            }
            R.id.menu2 -> {
                val f = Intent(this, FavouriteActivity::class.java)
                startActivity(f)
                return true
            }
            else -> return true
        }
    }

    private fun shearchDataUser(username: String) {
        searchUserModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(SearchViewModel::class.java)
        searchUserModel.setSearchUser(username)
        showLoading(true)
        searchUserModel.getSearchUsers().observe(this, { listSearchUser ->
            if (listSearchUser != null) {
                adapter.setData(listSearchUser)
                showLoading(false)
            }
        })
    }

    //ProgressBar
    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
        }
    }
}
