package com.example.submissionsatu.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.provider.ContactsContract
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionsatu.R
import com.example.submissionsatu.adapter.ListUserAdapter
import com.example.submissionsatu.databinding.ActivityMainBinding
import com.example.submissionsatu.model.User
import com.example.submissionsatu.viewmodel.MainViewModel
import com.example.submissionsatu.viewmodel.SearchViewModel

class MainActivity : AppCompatActivity() {
    private var listUser = ArrayList<User>()
    private lateinit var adapter: ListUserAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var getUserModel: MainViewModel
    private lateinit var searchUserModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "User GitHub"

        binding.rvUser

        showDataUsers()
        showRecyclerList()
        showLoading(true)

        //val observer: Observer<List<ContactsContract.Contacts.Data>>
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
        adapter = ListUserAdapter(listUser)
        binding.rvUser.adapter = adapter
        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user: User) {
        User(
            user.username,
            user.name,
            user.avatar,
            user.repository,
            user.company,
            user.location,
            user.followers,
            user.following,
        )
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
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) {
                    showDataUsers()
                } else {
                    shearchDataUser(newText)
                }
                return true
            }
        })
        return true
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
