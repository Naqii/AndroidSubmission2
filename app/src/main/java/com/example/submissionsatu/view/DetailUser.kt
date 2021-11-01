package com.example.submissionsatu.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.submissionsatu.R
import com.example.submissionsatu.adapter.SectionsPagerAdapter
import com.example.submissionsatu.databinding.ActivityDetailUserBinding
import com.example.submissionsatu.model.User
import com.example.submissionsatu.viewmodel.DetailViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUser : AppCompatActivity() {

    companion object {
        const val EXTRA_DATA = "extra_data"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var getUserDetail: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail User"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val userList = intent.getParcelableExtra<User>(EXTRA_DATA) as User
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
        getUserDetail.setDetailUser(userList.username)
        tabLayout()
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}