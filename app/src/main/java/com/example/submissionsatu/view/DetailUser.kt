package com.example.submissionsatu.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.submissionsatu.R
import com.example.submissionsatu.adapter.ListUserAdapter
import com.example.submissionsatu.adapter.SectionsPagerAdapter
import com.example.submissionsatu.database.Favourites
import com.example.submissionsatu.databinding.ActivityDetailUserBinding
import com.example.submissionsatu.helper.ViewModelFactory
import com.example.submissionsatu.model.User
import com.example.submissionsatu.ui.insert.FavouritesAddUpdateViewModel
import com.example.submissionsatu.viewmodel.DetailViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUser : AppCompatActivity() {

    companion object {
        const val EXTRA_DATA = "extra_data"
        const val EXTRA_FAV = "extra_favourite"
        const val ALERT_DIALOG_CLOSE = 10

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var getUserDetail: DetailViewModel

    private var sFavourites = false
    private var fav: Favourites? = Favourites()
    private lateinit var favAddUpdateViewModel: FavouritesAddUpdateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail User"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val userList = intent.getParcelableExtra(EXTRA_DATA) as? User
        fav = intent.getParcelableExtra(EXTRA_FAV)
        favAddUpdateViewModel = obtainViewModel(this@DetailUser)

        getUserDetail = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailViewModel::class.java)
        getUserDetail.getDetailUser().observe(this, { userDetail ->
            if (userDetail != null) {
                Log.d("tag", userDetail[0].toString())
                view(userDetail[0])
            }
        })
        if (userList != null) {
            getUserDetail.setDetailUser(userList.username)
        }
        tabLayout()


        binding.fabAdd.setOnClickListener {
            val username = binding.textUsername.text.toString().trim()
            val name = binding.textName.text.toString().trim()
            val avatar = userList?.avatar

            if (fav != null) {
                sFavourites = true
            } else {
                fav = Favourites()
            }

            if (sFavourites) {
                showAlertDialog(ALERT_DIALOG_CLOSE)
            } else {
                fav.let { favourites ->
                    favourites?.username = username
                    favourites?.name = name
                    favourites?.avatar = avatar.toString()
                }
                favAddUpdateViewModel.insert(fav as Favourites)
                showToast(getString(R.string.tambah))
            }
        }
    }

    private fun view(user: User) {
        with(binding) {
            Glide.with(this@DetailUser)
                .load(user.avatar)
                .apply(RequestOptions().override(150, 150))
                .into(imgAvatar)
            textName.text = user.name
            textUsername.text = user.username
            textLocation.text = user.location
            textRepository.text = user.repository
            textCompany.text = user.company
            showLoading(false)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

    private fun tabLayout() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }

    private fun showAlertDialog(type: Int) {
        val isDialogClose = type == ALERT_DIALOG_CLOSE
        val dialogTitle = getString(R.string.txt_hapus)
        val dialogMessage = getString(R.string.dialog_hapus)
        val alertDialogBuilder = AlertDialog.Builder(this)
        with(alertDialogBuilder) {
            setTitle(dialogTitle)
            setMessage(dialogMessage)
            setCancelable(false)
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                if (isDialogClose) {
                    favAddUpdateViewModel.delete(fav as Favourites)
                    showToast(getString(R.string.deleted))
                }
                finish()
            }
            setNegativeButton(getString(R.string.no)) { dialog, _ -> dialog.cancel() }
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavouritesAddUpdateViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavouritesAddUpdateViewModel::class.java)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}